package com.Attendance.student_sign_demo.service;

import com.Attendance.student_sign_demo.entity.Student;
import com.Attendance.student_sign_demo.form.FaceForm;
import com.Attendance.student_sign_demo.form.LoginForm;
import com.Attendance.student_sign_demo.form.RegisterForm;
import com.Attendance.student_sign_demo.vo.CourseVO;
import com.Attendance.student_sign_demo.vo.LoginVO;
import com.Attendance.student_sign_demo.vo.StudentAttendanceVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

public interface StudentService {
    //用户（老师/学生）登录接口
    public Future<LoginVO> checkLogin(LoginForm loginForm)throws Exception;
    public Future<Boolean> checkFace(String id)throws Exception;
    public Future<List<CourseVO>> getCourses(String id)throws Exception;
    public Future<List<StudentAttendanceVO>> getStudentAttendance(String courseId, String studentId)throws Exception;
    public Future<Boolean> updateFace(FaceForm faceForm)throws Exception;
    public Future<Boolean> addFace(String id,MultipartFile[] faceList)throws Exception;//上个函数的更改
    public Future<Boolean> register(RegisterForm registerForm)throws Exception;
   // public Future<Boolean> register(String userId,String password,String userClass,String address,String name)throws Exception;
    public Future<CourseVO> searchByCourseId(String courseId,String studentId)throws Exception;
    public Future<List<CourseVO>> searchByCourseName(String courseName,String studentId)throws Exception;
    public Future<Boolean> updatePassword(String studentId,String oldPassword,String newPassword)throws Exception;
    public Future<Boolean>  addAddress(String studentId,String address)throws Exception;
    public Future<Boolean>  resetPassword(String studentId,String password)throws Exception;
    public Future<List<byte[]>> getStudentFace(String studentId)throws Exception;
    public Future<CourseVO> searchByCourseIdForAdd(String studentId,String courseId)throws Exception;//查找所有课程
    public Future<List<CourseVO>> searchByCourseNameForAdd(String courseName,String studentId)throws Exception;//查找所有课程
    public Future<Boolean> joinCourse(String studentId,String courseId)throws Exception;
    public Future<String> getPassword(String id) throws Exception;
    public Future<String> getEmail(String id) throws Exception;
}
