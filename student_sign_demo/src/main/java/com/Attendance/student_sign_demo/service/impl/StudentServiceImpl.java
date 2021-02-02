package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.Attendance;
import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.entity.Student;
import com.Attendance.student_sign_demo.entity.Teacher;
import com.Attendance.student_sign_demo.form.FaceForm;
import com.Attendance.student_sign_demo.form.LoginForm;
import com.Attendance.student_sign_demo.form.RegisterForm;
import com.Attendance.student_sign_demo.repository.AttendanceRepository;
import com.Attendance.student_sign_demo.repository.CourseRepository;
import com.Attendance.student_sign_demo.repository.StudentRepository;
import com.Attendance.student_sign_demo.repository.TeacherRepository;
import com.Attendance.student_sign_demo.service.StudentService;
import com.Attendance.student_sign_demo.vo.Course1VO;
import com.Attendance.student_sign_demo.vo.CourseVO;
import com.Attendance.student_sign_demo.vo.LoginVO;
import com.Attendance.student_sign_demo.vo.StudentAttendanceVO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.Attendance.student_sign_demo.util.PathUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
//服务层：学生功能服务类
@Service
public class StudentServiceImpl implements StudentService {
    //main idea:控制反转
    //注入：对应的库操作

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    //学生登录函数(修改参数为表单类型)
    @Override
    public LoginVO checkLogin(LoginForm loginForm) {
        LoginVO loginVO=new LoginVO();
        String id = loginForm.getId();
        String pwd = loginForm.getPassword();
        if(id.length()==9)
        {//老师登录
            Teacher teacher=teacherRepository.findByTeacherNo(id);
            if(pwd.equals(teacher.getTeacherPassword()))
            {
                loginVO.setUserNO(teacher.getTeacherNo());
                loginVO.setUserName(teacher.getTeacherName());
                loginVO.setUserClass(null);
                loginVO.setUserAddress(teacher.getTeacherAddress());
                loginVO.setRole("老师");
            }
        }
        else
        {//学生登录
            Student student=studentRepository.findByStudentNo(id);
            if(pwd.equals(student.getStudentPassword()))
            {
                loginVO.setUserNO(student.getStudentNo());
                loginVO.setUserName(student.getStudentName());
                loginVO.setUserClass(student.getStudentClass());
                loginVO.setUserAddress(student.getStudentAddress());
                loginVO.setRole("学生");
            }
        }
        return loginVO;
    }

    //人脸信息是否存在接口实现
    @Override
    public boolean checkFace(String id) {
        if(studentRepository.findByStudentNo(id).getStudentEncoding()==null) {
            return false;
        }
        else return true;
    }

    //学生获取课程信息
    @Override
    public List<CourseVO> getCourses(String id) {
        String[] courses=studentRepository.findByStudentNo(id).getStudentCourse();
        List<CourseVO> courseVOList=new ArrayList<>();
        List<Course> courseList=new ArrayList<>();
        List<String> semesterList=new ArrayList<>();//新建学期列表
        for(int i=0;i<courses.length;i++)
        {
            Course course=courseRepository.findByCourseNo(courses[i]);
            if(semesterList.toArray().length==0){//列表为空直接添加
                semesterList.add(course.getCourseSemester());
            }
            else{
                if(!semesterList.contains(course.getCourseSemester())){
                    semesterList.add(course.getCourseSemester());//列表不为空比较后添加
                }
            }
            courseList.add(course);
        }
        for(int i=0;i<semesterList.toArray().length;i++){//遍历学期列表创建courseVO对象
            CourseVO courseVO=new CourseVO();
            courseVO.setSemester(semesterList.get(i));
            List<Course1VO> course1VOList=new ArrayList<>();
            for(int j=0;j<courseList.toArray().length;j++){//取出合适学期的课程
                if(courseList.get(j).getCourseSemester().equals(semesterList.get(i))){
                    Course course=courseList.get(j);
                    Course1VO course1VO=new Course1VO();
                    course1VO.setId(course.getCourseNo());
                    course1VO.setDays(course.getCourseDays());
                    course1VO.setEndTime(course.getCourseEndTime());
                    course1VO.setName(course.getCourseName());
                    course1VO.setWeeks(course.getCourseWeeks());
                    course1VO.setStartTime(course.getCourseStartTime());
                    course1VO.setCourseTeacher(teacherRepository.findByTeacherNo(course.getCourseTeacherNo()).getTeacherName());
                    course1VOList.add(course1VO);
                }
            }
            courseVO.setCourse1VOList(course1VOList);
            courseVOList.add(courseVO);
        }
        return courseVOList;
    }

    @Override
    public List<StudentAttendanceVO> getStudentAttendance(String courseId, String studentId) {
        List<StudentAttendanceVO> studentAttendanceVOList=new ArrayList<>();
        List<Attendance> attendanceList=attendanceRepository.findByAttendanceCourseNo(courseId);
        for(Attendance attendance:attendanceList){
            StudentAttendanceVO studentAttendanceVO=new StudentAttendanceVO();
            studentAttendanceVO.setTime(attendance.getAttendanceTime());
            studentAttendanceVO.setSuccess(attendance.getAttendanceActualStudent().contains(studentId));
            studentAttendanceVOList.add(studentAttendanceVO);
        }
        return studentAttendanceVOList;
    }

    //更新人脸信息
    @Override
    public Boolean updateFace(FaceForm faceForm) {
        //先将文件存到本地
        String studentNo = faceForm.getStudentId();
        MultipartFile faceImage = faceForm.getFaceImage();

        String filePath=PathUtil.demoPath+"/userFace/"+studentNo+".jpg"; //定义上传文件的存放位置
        try{
            //进行文件写入
            faceImage.transferTo(new File(filePath));
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        //运行python程序获得人脸encoding并保存在文件中
        String encodingFilePath=PathUtil.demoPath+"/userFace/encoding"+studentNo+".txt"; //定义encoding文件的存放位置
        try{
//            String exe="python";
//            String command="cd "+PathUtil.demoPath+" ; python face_encoding.py "+studentNo;
//            String[] cmdArr = new String[] { exe, command };
//            Process process = Runtime.getRuntime().exec(cmdArr);
            Process process = Runtime.getRuntime().exec(
                    "cmd.exe /c start "+PathUtil.demoPath+"/runEncode.bat "+studentNo);
            process.waitFor();
        }catch (Exception e){
            e.printStackTrace();
        }
        //从文件中读取encoding
        BufferedReader reader=null;
        StringBuffer stringBuffer=new StringBuffer();
        try{
            reader=new BufferedReader(new FileReader(new File(encodingFilePath)));
            String tempStr;
            while((tempStr=reader.readLine())!=null){
                stringBuffer.append(tempStr);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        File image=new File(filePath);
//        if(image.exists()){//删除图片
//            image.delete();
//        }
        Student student=studentRepository.findByStudentNo(studentNo);
        student.setStudentEncoding(stringBuffer.toString());
        studentRepository.save(student);//更新
        return true;
    }

    @Override
    public Boolean addFace(String id, MultipartFile[] faceList) {
        String studentNo=id;
        Student student=studentRepository.findByStudentNo(studentNo);
        String studentEncoding=student.getStudentEncoding()+";";
        String filePath=PathUtil.demoPath+"/userFace/"+studentNo+".jpg"; //定义上传文件的存放位置
        for(int i=0;i<faceList.length;i++)//依次创建人脸图
        {
            try{
                //进行文件写入
                faceList[i].transferTo(new File(filePath));
            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            String encodingFilePath=PathUtil.demoPath+"/userFace/encoding"+studentNo+".txt"; //定义encoding文件的存放位置
            try{
                Process process = Runtime.getRuntime().exec(
                        "cmd.exe /c start "+PathUtil.demoPath+"/runEncode.bat "+studentNo);
                process.waitFor();
            }catch (Exception e){
                e.printStackTrace();
            }
            //从文件中读取encoding
            BufferedReader reader=null;
            StringBuffer stringBuffer=new StringBuffer();
            try{
                reader=new BufferedReader(new FileReader(new File(encodingFilePath)));
                String tempStr;
                while((tempStr=reader.readLine())!=null){
                    stringBuffer.append(tempStr);
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            studentEncoding=studentEncoding+stringBuffer.toString()+";";
        }
        student.setStudentEncoding(studentEncoding.substring(0,studentEncoding.length()-1));
        studentRepository.save(student);//更新
        return true;
    }

    @Override
//    public boolean register(String userId, String password, String userClass, String address, String name) {
    public boolean register(RegisterForm registerForm) {
        String userId = registerForm.getId();
        String userClass = registerForm.getStudentClass();
        String name = registerForm.getName();
        String address = registerForm.getAddress();
        String password = registerForm.getPassword();
        if(userId.length()==13){
            Student student=new Student();
            student.setStudentNo(userId);
            student.setStudentClass(userClass);
            student.setStudentName(name);
            student.setStudentAddress(address);
            student.setStudentPassword(password);
            studentRepository.save(student);
        }
        else{
            Teacher teacher=new Teacher();
            teacher.setTeacherNo(userId);
            teacher.setTeacherAddress(address);
            teacher.setTeacherName(name);
            teacher.setTeacherPassword(password);
            teacherRepository.save(teacher);
        }
        return true;
    }

    @Override
    public CourseVO searchByCourseId(String id) {
        Course course=courseRepository.findByCourseNo(id);
        Course1VO course1VO=new Course1VO();
        course1VO.setCourseTeacher(teacherRepository.findByTeacherNo(course.getCourseTeacherNo()).getTeacherName());
        course1VO.setWeeks(course.getCourseWeeks());
        course1VO.setName(course.getCourseName());
        course1VO.setDays(course.getCourseDays());
        course1VO.setId(course.getCourseNo());
        course1VO.setStartTime(course.getCourseStartTime());
        course1VO.setEndTime(course.getCourseEndTime());
        List<Course1VO> course1VOList=new ArrayList<>();
        course1VOList.add(course1VO);
        CourseVO courseVO=new CourseVO();
        courseVO.setCourse1VOList(course1VOList);
        courseVO.setSemester(course.getCourseSemester());
        return courseVO;
    }

    @Override
    public CourseVO searchByCourseName(String courseName) {

        List<Course> courseList=courseRepository.findAll();
        Course1VO course1VO = new Course1VO();
        String semester = null;
        List<Course1VO> course1VOList = null;
        for(Course current : courseList){
            if(courseName.equals(current.getCourseName())){
                course1VO.setCourseTeacher(teacherRepository.findByTeacherNo(current.getCourseTeacherNo()).getTeacherName());
                course1VO.setWeeks(current.getCourseWeeks());
                course1VO.setName(current.getCourseName());
                course1VO.setDays(current.getCourseDays());
                course1VO.setId(current.getCourseNo());
                course1VO.setStartTime(current.getCourseStartTime());
                course1VO.setEndTime(current.getCourseEndTime());
                course1VOList=new ArrayList<>();
                course1VOList.add(course1VO);
                semester = current.getCourseSemester();
            }
        }
        CourseVO courseVO = new CourseVO();
        courseVO.setSemester(semester);
        courseVO.setCourse1VOList(course1VOList);
        return courseVO;
//        List<String> semesterList=new ArrayList<>();//新建学期列表
//        int k=courseList.toArray().length;
//        for(int i=0;i<k;i++)
//        {
//            Course course= courseList.get(i);
//            if(course.getCourseName().contains(courseName)){//符合条件判断学期
//                if(semesterList.toArray().length==0){//列表为空直接添加
//                    semesterList.add(course.getCourseSemester());
//                }
//                else{
//                    if(!semesterList.contains(course.getCourseSemester())){
//                        semesterList.add(course.getCourseSemester());//列表不为空比较后添加
//                    }
//                }
//            }
//            //不符合删除
//            else{
//                courseList.remove(i);
//                k=courseList.toArray().length;
//            }
//
//        }
//        List<CourseVO> courseVOList=new ArrayList<>();
//        for(int i = 0; i<semesterList.toArray().length; i++){//遍历学期列表创建courseVO对象
//            CourseVO courseVO=new CourseVO();
//            courseVO.setSemester(semesterList.get(i));
//            List<Course1VO> course1VOList=new ArrayList<>();
//            for(int j=0;j<courseList.toArray().length;j++){//取出合适学期的课程
//                if(courseList.get(j).getCourseSemester().equals(semesterList.get(i))){
//                    Course course=courseList.get(j);
//                    Course1VO course1VO=new Course1VO();
//                    course1VO.setId(course.getCourseNo());
//                    course1VO.setDays(course.getCourseDays());
//                    course1VO.setEndTime(course.getCourseEndTime());
//                    course1VO.setName(course.getCourseName());
//                    course1VO.setWeeks(course.getCourseWeeks());
//                    course1VO.setStartTime(course.getCourseStartTime());
//                    course1VO.setCourseTeacher(teacherRepository.findByTeacherNo(course.getCourseTeacherNo()).getTeacherName());
//                    course1VOList.add(course1VO);
//                }
//            }
//            courseVO.setCourse1VOList(course1VOList);
//            courseVOList.add(courseVO);
//        }
//        return courseVOList;
//    }
    }
}
