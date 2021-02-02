package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.form.LoginForm;
import com.Attendance.student_sign_demo.service.StudentService;
import com.Attendance.student_sign_demo.vo.CourseVO;
import com.Attendance.student_sign_demo.vo.LoginVO;
import com.Attendance.student_sign_demo.vo.StudentAttendanceVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Access;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StudentServiceImplTest {
    @Autowired
    private StudentService studentService;
    @Test
    void checkLogin() {
        LoginForm loginForm = new LoginForm("0121810880214","123456");
        LoginVO loginVO=studentService.checkLogin(loginForm);
        int a=0;
    }
    @Test
    void checkFaceInfo(){
        boolean result = studentService.checkFace("0121810880214");
        int a=0;
    }
    @Test
    void getCourses()
    {
        List<CourseVO> courseVOList=studentService.getCourses("0121810880214");
        int a=0;
    }
    @Test
    void getStudentAttendance()
    {
        List<StudentAttendanceVO> studentAttendanceVOList=studentService.getStudentAttendance("0000000001","0121810880214");
        int a=0;
    }
    @Test
    void register(){
        //studentService.register("000000004","123456",null,null,"亮亮");
    }
    @Test
    void searchByCourseId(){
        CourseVO courseVO=studentService.searchByCourseId("0000000001");
        int a=0;
    }
    @Test
    void searchByCourseName(){
        //List<CourseVO> courseVOList=studentService.searchByCourseName("C");
        int a=0;
    }
}