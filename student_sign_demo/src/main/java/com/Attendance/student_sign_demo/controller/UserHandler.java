package com.Attendance.student_sign_demo.controller;

import com.Attendance.student_sign_demo.exception.StudentException;
import com.Attendance.student_sign_demo.form.FaceForm;
import com.Attendance.student_sign_demo.form.LoginForm;
import com.Attendance.student_sign_demo.service.impl.StudentServiceImpl;
import com.Attendance.student_sign_demo.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.Attendance.student_sign_demo.util.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserHandler {

    @Autowired
    StudentServiceImpl studentService;

    //登录
    //@RequestBody注解，接收解析前段来的JSON
    @PostMapping("/login")
    public ResultVO login(
            @Valid @RequestBody LoginForm loginForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("【学生登录】参数错误");
            return ResultUtil.failed("学生账号和密码错误");
            //throw new StudentException(bindingResult.getFieldError().getDefaultMessage());
        }
        else{
            try {
                return ResultUtil.success(studentService.checkLogin(loginForm));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //注册：待完善
    @PutMapping("/register")
    public ResultVO register(){
        return null;
    }

    //更新学生人脸信息
    @PostMapping(path = "faceInfo")
    public ResultVO faceInfo(FaceForm faceForm,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            //System.out.println("【学生登录】参数错误");
            return ResultUtil.failed("学生账号和密码错误");
            //throw new StudentException(bindingResult.getFieldError().getDefaultMessage());
        }
        else{
            try {
                return ResultUtil.success(studentService.updateFace(faceForm));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //检查学生是否存在人脸信息
    @GetMapping("/checkFace/{id}")
    public ResultVO checkFace(@PathVariable("id") String id){
        if(id.length()!=13){
            return ResultUtil.failed("学生id不合法");
        }
        else {
            try {
                return ResultUtil.success(studentService.checkFace(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //测试类，检测get的多参数传值
    @GetMapping(path = "test")
    public ResultVO test(String a, Integer b){

        return ResultUtil.success(b);
    }
}
