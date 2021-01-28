package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.Attendance;
import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.entity.Student;
import com.Attendance.student_sign_demo.entity.Teacher;
import com.Attendance.student_sign_demo.form.FaceForm;
import com.Attendance.student_sign_demo.form.LoginForm;
import com.Attendance.student_sign_demo.repository.AttendanceRepository;
import com.Attendance.student_sign_demo.repository.CourseRepository;
import com.Attendance.student_sign_demo.repository.StudentRepository;
import com.Attendance.student_sign_demo.repository.TeacherRepository;
import com.Attendance.student_sign_demo.service.StudentService;
import com.Attendance.student_sign_demo.vo.CourseVO;
import com.Attendance.student_sign_demo.vo.LoginVO;
import com.Attendance.student_sign_demo.vo.StudentAttendanceVO;
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
        for(int i=0;i<courses.length;i++)
        {
            CourseVO courseVO=new CourseVO();
            Course course=courseRepository.findByCourseNo(courses[i]);
            courseVO.setId(course.getCourseNo());
            courseVO.setName(course.getCourseName());
            courseVO.setStartTime(course.getCourseStartTime());
            courseVO.setEndTime(course.getCourseEndTime());
            courseVO.setDays(course.getCourseDays());
            courseVO.setWeeks(course.getCourseWeeks());
            courseVO.setSemester(course.getCourseSemester());
            courseVO.setCourseTeacher(teacherRepository.findByTeacherNo(course.getCourseTeacherNo()).getTeacherName());
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
}
