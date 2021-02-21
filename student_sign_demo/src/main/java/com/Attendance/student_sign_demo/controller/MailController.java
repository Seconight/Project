package com.Attendance.student_sign_demo.controller;
import com.Attendance.student_sign_demo.service.impl.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
@Controller
public class MailController {
    @Autowired
    private MailService mailService;

    @RequestMapping("/getCheckCode")
    @ResponseBody
    public String getCheckCode(String email) {
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您的注册验证码为：" + checkCode;
        try {
            mailService.sendSimpleMail(email, "注册验证码", message);
        } catch (Exception e) {
            return "";
        }
        return checkCode;
    }
}
