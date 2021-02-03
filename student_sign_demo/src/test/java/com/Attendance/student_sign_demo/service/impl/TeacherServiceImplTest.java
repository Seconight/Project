package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.service.TeacherService;
import com.Attendance.student_sign_demo.vo.AttendanceVO;
import com.Attendance.student_sign_demo.vo.CourseStudentVO;
import com.Attendance.student_sign_demo.vo.TeacherCourseVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
class TeacherServiceImplTest {
    @Autowired
    private TeacherService teacherService;
    @Test
    void getCourses() throws Exception {
        Future<List<TeacherCourseVO>> listFuture=teacherService.getCourses("000000001");
        List<TeacherCourseVO> teacherCourseVOList=new ArrayList<>();
        while(true){
            if(listFuture.isDone()){
                teacherCourseVOList=listFuture.get();
                break;
            }
        }
        int a=0;
    }
    @Test
    void getCourseStudent() throws Exception {
        Future<List<CourseStudentVO>> courseStudentVOFuture=teacherService.getCourseStudent("0000000001");
        List<CourseStudentVO> courseStudentVOList=new ArrayList<>();
        while(true){
            if(courseStudentVOFuture.isDone()){
                courseStudentVOList=courseStudentVOFuture.get();
                break;
            }
        }
        int a=0;
    }
    @Test
    void getAttendanceInfo() throws Exception {
        Future<List<AttendanceVO>> attendanceVOFuture=teacherService.getAttendanceInfo("0000000001");
        List<AttendanceVO> attendanceVOList=new ArrayList<>();
        while(true){
            if(attendanceVOFuture.isDone()){
                attendanceVOList=attendanceVOFuture.get();
                break;
            }
        }
        int a=0;
    }
    @Test
    void supplyAttendance() throws Exception {
        Future<Boolean> booleanFuture=teacherService.supplyAttendance("0121810880207","0000000001");
        Boolean result=false;
        while(true){
            if(booleanFuture.isDone()){
                result=booleanFuture.get();
                break;
            }
        }
        int a=0;
    }
}