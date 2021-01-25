package com.Attendance.student_sign_demo.service;

import com.Attendance.student_sign_demo.vo.LoginVO;

public interface StudentService {
    public LoginVO checkLogin(String id, String pwd);
}
