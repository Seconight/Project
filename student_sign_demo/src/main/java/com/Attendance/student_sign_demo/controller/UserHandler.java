package com.Attendance.student_sign_demo.controller;

import com.Attendance.student_sign_demo.exception.StudentException;
import com.Attendance.student_sign_demo.form.FaceForm;
import com.Attendance.student_sign_demo.form.LoginForm;
import com.Attendance.student_sign_demo.form.RegisterForm;
import com.Attendance.student_sign_demo.service.impl.StudentServiceImpl;
import com.Attendance.student_sign_demo.service.impl.TeacherServiceImpl;
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

    @Autowired
    TeacherServiceImpl teacherService;

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
                return ResultUtil.success(studentService.checkLogin(loginForm).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //学生注册
    @PutMapping("/register")
    public ResultVO register(RegisterForm registerForm,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.failed("错误！出现了空信息！");
        }
        if(registerForm.getId().length()==13){
            if(registerForm.getStudentClass()==null){
                return ResultUtil.failed("学生信息不能为空");
            }
        }
        try {
            return ResultUtil.success(studentService.register(registerForm).get());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
                return ResultUtil.success(studentService.updateFace(faceForm).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //检查学生是否存在人脸信息
    @GetMapping("/checkFace")
    public ResultVO checkFace(String id){
        if(id.length()!=13){
            return ResultUtil.failed("学生id不合法");
        }
        else {
            try {
                return ResultUtil.success(studentService.checkFace(id).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //根据课程号获取课程信息
    @GetMapping("/searchByCourseId")
    public ResultVO searchByCourseId(String courseId,String studentId){
        if(courseId.length()!=10){
            return ResultUtil.failed("课程id不合法");
        }
        else{
            try {
                return ResultUtil.success(studentService.searchByCourseId(courseId,studentId).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //根据课程名获取课程信息
    @GetMapping("/searchByCourseName")
    public ResultVO searchByCourseName(String courseName,String studentId){
        if(courseName==null){
            return ResultUtil.failed("课程名为空！");
        }
        else{
            try {
                return ResultUtil.success(studentService.searchByCourseName(courseName,studentId).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //更新密码
    @PostMapping("/changePassword")
    public ResultVO changePassword(String id, String oldPassword, String newPassword){
        if(id.equals("") || id == null) {
            return ResultUtil.failed("id为空");
        }
        else{
            try{
                //由于前端为直接邮件验证身份，在此直接用新密码
                //用户为学生
                if(id.length() == 13){
                    return ResultUtil.success((studentService.resetPassword(id,newPassword)));
                    //return ResultUtil.success(studentService.updatePassword(id,oldPassword,newPassword).get());
                }
                //用户为老师
                else{
                    return ResultUtil.success(studentService.resetPassword(id,newPassword));
                    //return ResultUtil.success(teacherService.updatePassword(id,oldPassword,newPassword).get());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return ResultUtil.failed(null);
    }

    //添加邮箱
    @PostMapping("/addAddress")
    public ResultVO addAddress(String id, String address){
        if(id.equals("") || id == null) {
            return ResultUtil.failed("id为空");
        }
        else{
            try{
                //用户为学生
                if(id.length() == 13){
                    return ResultUtil.success(studentService.addAddress(id,address));
                }
                //用户为老师
                else{
                    return ResultUtil.success(teacherService.addAddress(id, address));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //重置密码
    @PostMapping("/resetPassword")
    public ResultVO resetPassword(String id, String password){
        if(id.equals("") || id == null) {
            return ResultUtil.failed("id为空");
        }
        else{
            try{
                //用户为学生
                if(id.length() == 13){
                    return ResultUtil.success(studentService.resetPassword(id,password));
                }
                //用户为老师
                else{
                    return ResultUtil.success((teacherService.resetPassword(id, password)));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //获取人脸信息
    @GetMapping("/getFaces")
    public ResultVO getFaces(String id){
        if(id.equals("") || id == null) {
            return ResultUtil.failed("id为空");
        }
        else{
            try{
                return ResultUtil.success(studentService.getStudentFace(id).get());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return ResultUtil.failed(null);
    }

    //获得密码
    @GetMapping("/getPassword")
    public ResultVO getPassword(String id){
        if(id.equals("") || id == null) {
            return ResultUtil.failed("id为空");
        }
        else{
            try{
                //用户为学生
                if(id.length() == 13){
                    return ResultUtil.success(studentService.getPassword(id).get());
                }
                //用户为老师
                else{
                    return ResultUtil.success(teacherService.getPassword(id).get());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //获得email
    @GetMapping("/getEmail")
    public ResultVO getEmail(String id){
        if(id.equals("") || id == null) {
            return ResultUtil.failed("id为空");
        }
        else{
            try{
                //用户为学生
                if(id.length() == 13){
                    return ResultUtil.success(studentService.getEmail(id).get());
                }
                //用户为老师
                else{
                    return ResultUtil.success(teacherService.getEmail(id).get());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //学生选课搜索课程 by ID
    @GetMapping("searchForAddById")
    public ResultVO searchForAddById(String studentId,String courseId){
        if(studentId.equals("")||courseId.equals("")||studentId==null||courseId==null){
            return ResultUtil.failed("id为空！");
        }
        else{
            try {
                return ResultUtil.success(studentService.searchByCourseIdForAdd(studentId, courseId).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //学生选课搜索课程 by Name
    @GetMapping("searchForAddByName")
    public ResultVO searchForAddByName(String courseName, String studentId){
        if(studentId.equals("")||studentId == null){
            return ResultUtil.failed("id为空！");
        }
        else{
            try {
                return ResultUtil.success(studentService.searchByCourseNameForAdd(courseName, studentId).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //加入课程
    @PostMapping("joinCourse")
    public ResultVO joinCourse(String studentId, String courseId){
        if(studentId.equals("")||courseId.equals("")||studentId==null||courseId==null){
            return ResultUtil.failed("id为空");
        }
        else{
            try {
                return ResultUtil.success(studentService.joinCourse(studentId, courseId).get());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //测试类，检测get的多参数传值
    @GetMapping(path = "test")
    public ResultVO test(String a, Integer b){

        return ResultUtil.success(b);
    }
}
