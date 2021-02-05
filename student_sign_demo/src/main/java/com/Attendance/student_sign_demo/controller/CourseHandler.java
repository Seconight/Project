package com.Attendance.student_sign_demo.controller;

import com.Attendance.student_sign_demo.service.impl.StudentServiceImpl;
import com.Attendance.student_sign_demo.util.ResultUtil;
import com.Attendance.student_sign_demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/course")
public class CourseHandler {

    //数据控制注入
    @Autowired
    private StudentServiceImpl studentService;

    //学生获取课程
    @GetMapping(path = "info")
    public ResultVO info(String id){
        if(id==null || id.length()!=13){
            return ResultUtil.failed("学生id不合法,请稍后重试!");
        }
        else {
            try {
                return ResultUtil.success(studentService.getCourses(id).get());//处理成功未出错返回正确结果
            } catch (SQLException e) {
                return ResultUtil.failed("服务器数据库异常，请稍后重试!");//出现异常返回错误信息
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return ResultUtil.failed("线程被中断，请稍后重试!");
            }catch (ExecutionException e){
                return ResultUtil.failed("线程I/O异常，请稍后重试!");
            }catch (Exception e){
                return ResultUtil.failed(e.getMessage()+"\n"+"请稍后重试!");
            }
        }
    }

    //学生查看课程签到信息
    @GetMapping(path = "attendanceInfo")
    public ResultVO attendanceInfo(String courseId, String studentId){
        if(courseId==null||courseId.length()!=10){
            return ResultUtil.failed("课程id出错，请稍后重试!");
        }
        else if(studentId==null||studentId.length()!=13){
            return ResultUtil.failed("学生id出错，请稍后重试!");
        }
        else {
            try {
                return ResultUtil.success(studentService.getStudentAttendance(courseId,studentId).get());
            }catch (SQLException e) {
                return ResultUtil.failed("服务器数据库异常，请稍后重试!");//出现异常返回错误信息
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return ResultUtil.failed("线程被中断，请稍后重试!");
            }catch (ExecutionException e){
                return ResultUtil.failed("线程I/O异常，请稍后重试!");
            }catch (Exception e) {
                return ResultUtil.failed(e.getMessage()+"\n"+"请稍后重试!");
            }
        }
    }
}
