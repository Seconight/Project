package com.Attendance.student_sign_demo.controller;

import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.exception.StudentException;
import com.Attendance.student_sign_demo.form.CourseForm;
import com.Attendance.student_sign_demo.service.impl.TeacherServiceImpl;
import com.Attendance.student_sign_demo.util.ResultUtil;
import com.Attendance.student_sign_demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/teacher")

public class TeacherHandler {

    @Autowired
    TeacherServiceImpl teacherService;

    //老师新建课程
    @PostMapping("/createCourse")
    public ResultVO createCourse(
            @RequestBody CourseForm courseForm){
//        if(bindingResult.hasErrors()){
//            return ResultUtil.failed(bindingResult.getFieldError().getDefaultMessage());
//        }
        teacherService.newCourse(courseForm);
        return ResultUtil.success(null);
    }
}
