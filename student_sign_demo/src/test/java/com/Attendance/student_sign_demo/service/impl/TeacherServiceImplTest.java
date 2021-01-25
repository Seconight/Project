package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.service.TeacherService;
import com.Attendance.student_sign_demo.vo.AttendanceVO;
import com.Attendance.student_sign_demo.vo.CourseStudentVO;
import com.Attendance.student_sign_demo.vo.TeacherCourseVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TeacherServiceImplTest {
    @Autowired
    TeacherService teacherService;
    @Test
    void getCourses() {
        List<TeacherCourseVO> teacherCourseVOList=teacherService.getCourses("000000001");
        int a=0;
    }
    @Test
    void getCourseStudent(){
        List<CourseStudentVO> courseStudentVOList=teacherService.getCourseStudent("0000000004");
        int a=0;
    }
    @Test
    void getAttendanceInfo(){
        List<AttendanceVO> attendanceVOList=teacherService.getAttendanceInfo("0000000001");
        int a=0;
    }
    @Test
    void supplyAttendance(){
        Boolean result=teacherService.supplyAttendance("0121810880207","0000000001");
        int a=0;
    }
}