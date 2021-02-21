package com.Attendance.student_sign_demo.service;

import com.Attendance.student_sign_demo.service.impl.MailService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class MailServiceTest {
    @Autowired
    MailService mailService;
    @Test
    void sendSimpleMail() {
        mailService.sendSimpleMail("614300076@qq.com","验证码","407869");
    }
}