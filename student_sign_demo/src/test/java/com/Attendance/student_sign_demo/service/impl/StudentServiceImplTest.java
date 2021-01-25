package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.service.StudentService;
import com.Attendance.student_sign_demo.vo.LoginVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Access;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StudentServiceImplTest {
    @Autowired
    private StudentService studentService;
    @Test
    void checkLogin() {
        LoginVO loginVO=studentService.checkLogin("000000001","123456");
        int a=0;
    }
}