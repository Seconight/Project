package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.form.LoginForm;
import com.Attendance.student_sign_demo.service.StudentService;
import com.Attendance.student_sign_demo.vo.CourseVO;
import com.Attendance.student_sign_demo.vo.LoginVO;
import com.Attendance.student_sign_demo.vo.StudentAttendanceVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Access;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

//@EnableTransactionManagement(proxyTargetClass = true)
@RunWith(SpringRunner.class)
@SpringBootTest
class StudentServiceImplTest {
    @Autowired
    private StudentService studentService;
    @Test
    void checkLogin() throws Exception {
        LoginForm loginForm = new LoginForm("0121810880214","123456");
        Future<LoginVO> loginVOFuture=studentService.checkLogin(loginForm);
        LoginVO loginVO=new LoginVO();
        while(true){
            if(loginVOFuture.isDone()){
                loginVO=loginVOFuture.get();
                break;
            }
        }
        int a=0;
    }
    @Test
    void checkFaceInfo() throws Exception {
        Future<Boolean> booleanFuture=studentService.checkFace("0121810880214");
        Boolean result =false;
        while(true){
            if(booleanFuture.isDone()){
                result=booleanFuture.get();
                break;
            }
        }
        int a=0;
    }
    @Test
    void getCourses() throws Exception {
    Future<List<CourseVO>> courseVOFuture=studentService.getCourses("0121810880207");
    List<CourseVO> courseVOList=new ArrayList<>();
    while(true){
        if(courseVOFuture.isDone()){
            courseVOList=courseVOFuture.get();
            break;
        }
    }
    for(CourseVO courseVO:courseVOList){
        System.out.println(courseVO);
    }
     }
    @Test
    void getStudentAttendance() throws Exception {
    Future<List<StudentAttendanceVO>> studentAttendanceVOFuture=studentService.getStudentAttendance("0000000001","0121810880214");
    List<StudentAttendanceVO> studentAttendanceVOList=new ArrayList<>();
    while(true){
        if(studentAttendanceVOFuture.isDone()){
            studentAttendanceVOList=studentAttendanceVOFuture.get();
            break;
        }
    }
    int a=0;
     }
    @Test
    void register() throws Exception {
        //studentService.register("000000004","123456",null,null,"亮亮");
    }
    @Test
    void searchByCourseId() throws Exception {
        Future<CourseVO> courseVOFuture=studentService.searchByCourseId("0000000002","0121810880201");
        CourseVO courseVO=new CourseVO();
        while(true){
            if(courseVOFuture.isDone())
            {
                courseVO=courseVOFuture.get();
                break;
            }
        }
        System.out.println(courseVO);
        int a=0;
    }
    @Test
    void searchByCourseName() throws Exception {

        Future<List<CourseVO>> courseVOFuture=studentService.searchByCourseName("C","0121810880201");
        
        List<CourseVO> courseVOList=new ArrayList<>();
        while(true){
            if(courseVOFuture.isDone()){
                courseVOList=courseVOFuture.get();
                break;
            }
        }
        for(CourseVO courseVO:courseVOList){
            System.out.println(courseVO);
        }
        int a=0;
    }
}