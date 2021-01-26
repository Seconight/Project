package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.Attendance;
import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.entity.Student;
import com.Attendance.student_sign_demo.entity.Teacher;
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public LoginVO checkLogin(String id, String pwd) {
        LoginVO loginVO=new LoginVO();
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
        if(studentRepository.faceInfoIsNull(id)==null)
        return false;
        else return true;
    }

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

    @Override
    public Boolean updateFace(String studentNo, MultipartFile faceImage) {
        //先将文件存到本地
        String filePath="E:/360Downloads/计算机设计大赛/cdc_face/keras-face-recognition-master/userFace/face.jpg"; //定义上传文件的存放位置
        try{
            faceImage.transferTo(new File(filePath));
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        //运行python程序获得人脸encoding并保存在文件中
        String encodingFilePath="E:/360Downloads/计算机设计大赛/cdc_face/keras-face-recognition-master/userFace/encoding.txt"; //定义encoding文件的存放位置
        try{
            String exe="python";
            String command="E:/360Downloads/计算机设计大赛/cdc_face/keras-face-recognition-master/face_encoding.py";
            String[] cmdArr = new String[] { exe, command };
            Process process = Runtime.getRuntime().exec(cmdArr);
        }catch (IOException e){
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
        File image=new File(filePath);
        if(image.exists()){//删除图片
            image.delete();
        }
        Student student=studentRepository.findByStudentNo(studentNo);
        student.setStudentEncoding(stringBuffer.toString());
        studentRepository.save(student);//更新
        return true;
    }
}
