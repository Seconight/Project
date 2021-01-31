package com.Attendance.student_sign_demo.controller;

import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.exception.StudentException;
import com.Attendance.student_sign_demo.form.AttendanceForm;
import com.Attendance.student_sign_demo.form.CourseForm;
import com.Attendance.student_sign_demo.service.impl.TeacherServiceImpl;
import com.Attendance.student_sign_demo.util.ResultUtil;
import com.Attendance.student_sign_demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/teacher")

//TODO:提高健壮性

public class TeacherHandler {

    @Autowired
    TeacherServiceImpl teacherService;

    //老师新建课程
    @PutMapping("/createCourse")
    public ResultVO createCourse(
           CourseForm courseForm,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.failed(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        teacherService.newCourse(courseForm);
        return ResultUtil.success(null);
    }

    //老师获取课程信息
    @GetMapping(path = "/getCourseInfo")
    public ResultVO getCourseInfo(String id){
        if(id.length() != 9){
            return ResultUtil.failed("老师id不合法");
        }
        else {
            return ResultUtil.success(teacherService.getCourses(id));
        }
    }

    //老师查看对应课程学生
    @GetMapping(path = "/getCourseStudent")
    public ResultVO getCourseStudent(String id){
        if(id.length() != 10){
            return ResultUtil.failed("课程id不合法");
        }
        else {
            return ResultUtil.success(teacherService.getCourseStudent(id));
        }
    }

    //老师拍照签到
    @PostMapping(path = "/attendance")
    public ResultVO attendance(AttendanceForm attendanceForm,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.failed(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ResultUtil.success(teacherService.Sign(attendanceForm));
    }

    //老师获取签到信息
    @GetMapping(path = "/checkAttendanceInfo")
    public ResultVO checkAttendanceInfo(String id){
        if(id.length() != 10){
            return ResultUtil.failed("签到信息id不合法");
        }
        return ResultUtil.success(teacherService.getAttendanceInfo(id));
    }

    //老师补签
    @PostMapping(path = "/supply")
    public ResultVO supply(String studentId, String attendanceId){
        if(studentId.length()!=13){
            return ResultUtil.failed("学生id不合法");
        }
        if(attendanceId.length()!=10){
            return ResultUtil.failed("学生id不合法");
        }
        return ResultUtil.success(teacherService.supplyAttendance(studentId,attendanceId));
    }
}
