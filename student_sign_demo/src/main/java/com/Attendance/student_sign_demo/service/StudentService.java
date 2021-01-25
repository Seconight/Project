package com.Attendance.student_sign_demo.service;

import com.Attendance.student_sign_demo.entity.Student;
import com.Attendance.student_sign_demo.vo.LoginVO;

public interface StudentService {
    //用户（老师/学生）登录接口
    public LoginVO checkLogin(String id, String pwd);
    //检测是否有人脸信息
    public boolean checkFace(String id);
}
