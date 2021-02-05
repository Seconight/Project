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
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/teacher")

//TODO:提高健壮性

public class TeacherHandler {

    @Autowired
    private
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
        if(id==null||id.length() != 9){
            return ResultUtil.failed("老师id出错，请稍后重试!");
        }
        else {
            try {
                return ResultUtil.success(teacherService.getCourses(id).get());
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

    //老师查看对应课程学生
    @GetMapping(path = "/getCourseStudent")
    public ResultVO getCourseStudent(String id){
        if(id==null||id.length() != 10){
            return ResultUtil.failed("课程id出错，请稍后重试!");
        }
        else {
            try {
                return ResultUtil.success(teacherService.getCourseStudent(id).get());
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

    //老师拍照签到
    @PostMapping(path = "/attendance")
    public ResultVO attendance(AttendanceForm attendanceForm,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.failed(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        else{
            try {
                return ResultUtil.success(teacherService.Sign(attendanceForm).get());
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

    //老师获取签到信息
    @GetMapping(path = "/checkAttendanceInfo")
    public ResultVO checkAttendanceInfo(String id){
        if(id==null||id.length() != 10){
            return ResultUtil.failed("签到id出错，请稍后重试!");
        }
        else{
            try {
                return ResultUtil.success(teacherService.getAttendanceInfo(id).get());
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

    //老师补签
    @PostMapping(path = "/supply")
    public ResultVO supply(String studentId, String attendanceId){
        if(studentId==null||studentId.length()!=13){
            return ResultUtil.failed("学生id出错，请稍后重试!");
        }
        else if(attendanceId==null||attendanceId.length()!=10){
            return ResultUtil.failed("学生id出错，，请稍后重试!");
        }
        else{
            try {
                return ResultUtil.success(teacherService.supplyAttendance(studentId,attendanceId).get());
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
}
