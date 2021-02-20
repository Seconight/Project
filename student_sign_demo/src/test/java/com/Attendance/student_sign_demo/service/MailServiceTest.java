package com.Attendance.student_sign_demo.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class MailServiceTest {
    @Autowired
    MailService mailService;
    @Test
    void sendSimpleMail() {
        mailService.sendSimpleMail("2190459043@qq.com","验证码","407869");
    }
}