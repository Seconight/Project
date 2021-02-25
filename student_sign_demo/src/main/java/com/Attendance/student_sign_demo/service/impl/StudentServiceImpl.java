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
import com.Attendance.student_sign_demo.util.CommandUtil;
import com.Attendance.student_sign_demo.vo.Course1VO;
import com.Attendance.student_sign_demo.vo.CourseVO;
import com.Attendance.student_sign_demo.vo.LoginVO;
import com.Attendance.student_sign_demo.vo.StudentAttendanceVO;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.Attendance.student_sign_demo.util.PathUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.io.File;
import java.io.FileInputStream;


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
    @Async("asyncServiceExecutor")
    public Future<LoginVO> checkLogin(LoginForm loginForm) throws Exception{
        LoginVO loginVO=new LoginVO();
        String id = loginForm.getId();
        String pwd = loginForm.getPassword();
        if(id.length()==9)
        {//老师登录
            Teacher teacher=teacherRepository.findByTeacherNo(id);
            //当数据库异常的时候上一句会直接产生异常，不会往下执行，这时候如果想报错给前端该怎么做
            if(teacher.getTeacherPassword()!=null&&!teacher.getTeacherPassword().equals("")&&pwd.equals(teacher.getTeacherPassword()))
            {//密码不为空，不为空字符串
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
            if(student.getStudentPassword()!=null&&!student.getStudentPassword().equals("")&&pwd.equals(student.getStudentPassword()))
            {
                loginVO.setUserNO(student.getStudentNo());
                loginVO.setUserName(student.getStudentName());
                loginVO.setUserClass(student.getStudentClass());
                loginVO.setUserAddress(student.getStudentAddress());
                loginVO.setRole("学生");
            }
        }
        return new AsyncResult<>(loginVO);
    }

    //人脸信息是否存在接口实现
    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> checkFace(String id) throws Exception{
        String encoding=studentRepository.findByStudentNo(id).getStudentEncoding();
        if(encoding==null||encoding.equals("")){
            return new AsyncResult<>(false);
        }
        else return new AsyncResult<>(true);
    }

    //学生获取课程信息
    @Override
    @Async("asyncServiceExecutor")
    public Future<List<CourseVO>> getCourses(String id)throws Exception {
        String[] courses=studentRepository.findByStudentNo(id).getStudentCourse();
        List<CourseVO> courseVOList=new ArrayList<>();
        if(courses==null||courses.length==0){//当课程不存在或者未获取到返回空列表
            //不做任何处理
        }
        else {
            List<Course> courseList = new ArrayList<>();
            List<String> semesterList = new ArrayList<>();//新建学期列表
            for (int i = 0; i < courses.length; i++) {//删除重复学期，获得学期列表
                Course course = courseRepository.findByCourseNo(courses[i]);
                if (semesterList.toArray().length == 0) {//列表为空直接添加
                    semesterList.add(course.getCourseSemester());
                } else {
                    if (!semesterList.contains(course.getCourseSemester())) {//当前学期列表中存在当前课程学期则不添加当前学期
                        semesterList.add(course.getCourseSemester());//列表不为空比较后添加
                    }
                }
                courseList.add(course);
            }
            for (int i = 0; i < semesterList.toArray().length;i++) {//遍历学期列表和课程列表创建courseVO对象
                CourseVO courseVO = new CourseVO();
                courseVO.setSemester(semesterList.get(i));
                List<Course1VO> course1VOList = new ArrayList<>();
                for (int j = 0; j <courseList.toArray().length;j++ ) {//取出合适学期的课程
                    if (courseList.get(j).getCourseSemester().equals(semesterList.get(i))) {
                        Course course = courseList.get(j);
                        Course1VO course1VO = new Course1VO();
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
        }
        return new AsyncResult<>(courseVOList);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<List<StudentAttendanceVO>> getStudentAttendance(String courseId, String studentId) throws Exception{
        List<StudentAttendanceVO> studentAttendanceVOList=new ArrayList<>();
        List<Attendance> attendanceList=attendanceRepository.findByAttendanceCourseNo(courseId);
        for(Attendance attendance:attendanceList){
            StudentAttendanceVO studentAttendanceVO=new StudentAttendanceVO();
            studentAttendanceVO.setTime(attendance.getAttendanceTime());
            studentAttendanceVO.setSuccess(attendance.getAttendanceActualStudent().contains(studentId));
            studentAttendanceVOList.add(studentAttendanceVO);
        }
        return new AsyncResult<>(studentAttendanceVOList);
    }

    //更新人脸信息
    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> updateFace(FaceForm faceForm) throws Exception {
        //先将文件存到本地
        String studentNo = faceForm.getStudentId();
        MultipartFile[] faceImage = faceForm.getFaceImage();
        String encodings="";
        for(int i=0;i<faceImage.length;i++){//遍历人脸列表并计算128维特征向量

            //对应学生以学号存入文件夹 D:/xxxx/userface/0121810880207
            String imagePath = PathUtil.demoPath+"/userFace/"+studentNo;
            File pathDir = new File(imagePath);
            if(!pathDir.exists()){
                pathDir.mkdir();
            }

            //学生文件名，格式eg : 0121810880207_1.jpg
            //与文件夹相接加上‘/’
            String fileName = studentNo+"_"+String.valueOf(i)+".jpg";

            //String filePath=PathUtil.demoPath+"/userFace/"+studentNo+String.valueOf(i)+".jpg"; //定义上传文件的存放位置//这里多加了人脸序号
            try{
                //进行文件写入
                faceImage[i].transferTo(new File(imagePath+"/"+fileName));

            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            //运行python程序获得人脸encoding并保存在文件中
            String encodingFilePath=imagePath+"/encoding"+studentNo+"_"+String.valueOf(i)+".txt"; //定义encoding文件的存放位置
            try{
                CommandUtil commandUtil = new CommandUtil();
                commandUtil.executeCommand("cmd.exe /c start "+PathUtil.demoPath+"/runEncode.bat "+studentNo+" "+i);
            }catch (Exception e){
                e.printStackTrace();
            }
            File code = new File(encodingFilePath);
            while(!code.exists()){
                //do nothing
                //&wait for getting the code
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
            encodings=encodings+stringBuffer.toString()+";";
            if(code.exists()){
                code.delete();
            }
        }

        Student student=studentRepository.findByStudentNo(studentNo);
        student.setStudentEncoding(encodings.substring(0,encodings.length()-1));
        studentRepository.save(student);//更新
        return new AsyncResult<>(true);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> addFace(String id, MultipartFile[] faceList)throws Exception {
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
        return new AsyncResult<>(true);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> register(RegisterForm registerForm)throws Exception {
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
        return new AsyncResult<>(true);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<CourseVO> searchByCourseId(String courseId,String studentId)throws Exception {
        Course course=courseRepository.findByCourseNo(courseId);
        Student student=studentRepository.findByStudentNo(studentId);
        if(course==null||!student.getStudentCourses().contains(courseId))
        {
            return null;
        }
        else{
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
            return new AsyncResult<>(courseVO);
        }

    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<List<CourseVO>> searchByCourseName(String courseName,String studentId)throws Exception {
        List<Course> courseList1=new ArrayList<>();
        Student student=studentRepository.findByStudentNo(studentId);
        String []courses=student.getStudentCourse();
        for(String courseId:courses){
            courseList1.add(courseRepository.findByCourseNo(courseId));
        }
        List<String> semesterList=new ArrayList<>();//新建学期列表
        for(int i=0;i<courseList1.toArray().length;i++)
        {
            Course course= courseList1.get(i);
            if(course.getCourseName().contains(courseName)){//符合条件判断学期
                if(semesterList.toArray().length==0){//列表为空直接添加
                    semesterList.add(course.getCourseSemester());
                }
                else{
                    if(!semesterList.contains(course.getCourseSemester())){
                        semesterList.add(course.getCourseSemester());//列表不为空比较后添加
                    }
                }
            }
        }
        List<CourseVO> courseVOList=new ArrayList<>();
        for(int i = 0; i<semesterList.toArray().length; i++){//遍历学期列表创建courseVO对象
            CourseVO courseVO=new CourseVO();
            courseVO.setSemester(semesterList.get(i));
            List<Course1VO> course1VOList=new ArrayList<>();
            for(int j=0;j<courseList1.toArray().length;j++){//取出合适学期的课程
                if(courseList1.get(j).getCourseName().contains(courseName)&&courseList1.get(j).getCourseSemester().equals(semesterList.get(i))){
                    Course course=courseList1.get(j);
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
        return new AsyncResult<>(courseVOList);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> updatePassword(String studentId,String oldPassword, String newPassword) throws Exception {
        Student student=studentRepository.findByStudentNo(studentId);
        String password=student.getStudentPassword();
        if(password!=null&&!password.equals("")&&password.equals(oldPassword)){
            student.setStudentPassword(newPassword);
            studentRepository.save(student);
            return new AsyncResult<>(true);
        }
        return new AsyncResult<>(false);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> addAddress(String studentId, String address) throws Exception {
        Student student=studentRepository.findByStudentNo(studentId);
        student.setStudentAddress(address);
        studentRepository.save(student);
        return new AsyncResult<>(true);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> resetPassword(String studentId, String password) throws Exception {
        Student student=studentRepository.findByStudentNo(studentId);
        student.setStudentPassword(password);
        studentRepository.save(student);
        return new AsyncResult<>(true);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<List<MultipartFile>> getStudentFace(String studentId) throws Exception {
        String path="E:\\360Downloads\\计算机设计大赛\\学生人脸\\"+studentId;//文件夹路径
        File file = new File(path);
        File[] files = file.listFiles();//获取所有文件1li
        if(files!=null){
            List<MultipartFile> multipartFiles= new ArrayList<>();
            for(File eachFile :files){
                FileInputStream fileInputStream = new FileInputStream(eachFile);
                multipartFiles.add(new MockMultipartFile(eachFile.getName(), eachFile.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream));
            }
            return new AsyncResult<>(multipartFiles);
        }
        return null;
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<CourseVO> searchByCourseIdForAdd(String studentId, String courseId) throws Exception {
        Course course=courseRepository.findByCourseNo(courseId);
        System.out.println(course.getCourseName());
        Student student=studentRepository.findByStudentNo(studentId);
        if(course!=null&&course.getCourseStudent().contains(studentId)&&!student.getStudentCourses().contains(courseId))
        {
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
            return new AsyncResult<>(courseVO);
        }
        else{
            return null;
        }
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<List<CourseVO>> searchByCourseNameForAdd(String courseName, String studentId) throws Exception {
        List<Course> courseList1=courseRepository.findAll();
        Student student=studentRepository.findByStudentNo(studentId);
        String courses=student.getStudentCourses();//当前学生已经添加的课程
        List<Course> courseList=new ArrayList<>();
        for(int i=0;i<courseList1.toArray().length;i++){
            if(!courses.contains(courseList1.get(i).getCourseNo())&&courseList1.get(i).getCourseStudent().contains(studentId)){
                courseList.add(courseList1.get(i));
            }
        }
        for(int i=0;i<courseList.toArray().length;i++){
            System.out.println((courseList.get(i)));
        }
        List<String> semesterList=new ArrayList<>();//新建学期列表
        for(int i=0;i<courseList.toArray().length;i++)
        {
            Course course= courseList.get(i);
            if(course.getCourseName().contains(courseName)){//符合条件判断学期
                if(semesterList.toArray().length==0){//列表为空直接添加
                    semesterList.add(course.getCourseSemester());
                }
                else{
                    if(!semesterList.contains(course.getCourseSemester())){
                        semesterList.add(course.getCourseSemester());//列表不为空比较后添加
                    }
                }
            }
        }
        List<CourseVO> courseVOList=new ArrayList<>();
        for(int i = 0; i<semesterList.toArray().length; i++){//遍历学期列表创建courseVO对象
            CourseVO courseVO=new CourseVO();
            courseVO.setSemester(semesterList.get(i));
            List<Course1VO> course1VOList=new ArrayList<>();
            for(int j=0;j<courseList.toArray().length;j++){//取出合适学期的课程
                if(courseList.get(j).getCourseName().contains(courseName)&&courseList.get(j).getCourseSemester().equals(semesterList.get(i))){
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
        return new AsyncResult<>(courseVOList);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> joinCourse(String studentId, String courseId) throws Exception {
        Student student=studentRepository.findByStudentNo(studentId);
        String courses=student.getStudentCourses();
        if(courses==null||courses.equals("")){
            student.setStudentCourse(courseId);
        }
        else {
            student.setStudentCourse(courses+","+courseId);
        }
        studentRepository.save(student);
        return new AsyncResult<>(true);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<String> getPassword(String id) throws Exception{
        Student student = studentRepository.findByStudentNo(id);
        String password = student.getStudentPassword();
        return new AsyncResult<>(password);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<String> getEmail(String id) throws Exception{
        Student student = studentRepository.findByStudentNo(id);
        String email = student.getStudentAddress();
        return new AsyncResult<>(email);
    }

}
