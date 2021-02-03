package com.Attendance.student_sign_demo.controller;

import com.Attendance.student_sign_demo.service.impl.StudentServiceImpl;
import com.Attendance.student_sign_demo.util.ResultUtil;
import com.Attendance.student_sign_demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseHandler {

    //数据控制注入
    @Autowired
    private StudentServiceImpl studentService;

    //学生获取课程
    @GetMapping(path = "info")
    public ResultVO info(String id){
        if(id.length()!=13){
            return ResultUtil.failed("课程id不合法");
        }
        else {
            try {
                return ResultUtil.success(studentService.getCourses(id));//这里差学期参数
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //学生查看课程签到信息
    @GetMapping(path = "attendanceInfo")
    public ResultVO attendanceInfo(String courseId, String studentId){
        if(courseId.length()!=10){
            return ResultUtil.failed("课程id不合法");
        }
        else if(studentId.length()!=13){
            return ResultUtil.failed("学生id不合法");
        }
        else {
            try {
                return ResultUtil.success(studentService.getStudentAttendance(courseId,studentId));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
