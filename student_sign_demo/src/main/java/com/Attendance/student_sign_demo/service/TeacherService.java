package com.Attendance.student_sign_demo.service;

import com.Attendance.student_sign_demo.form.AttendanceForm;
import com.Attendance.student_sign_demo.form.CourseForm;
import com.Attendance.student_sign_demo.vo.AttendanceVO;
import com.Attendance.student_sign_demo.vo.CourseStudentVO;
import com.Attendance.student_sign_demo.vo.CourseVO;
import com.Attendance.student_sign_demo.vo.TeacherCourseVO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.concurrent.Future;

public interface TeacherService {
    public Future<List<TeacherCourseVO>> getCourses(String teacherId)throws Exception;
    public Future<List<CourseStudentVO>> getCourseStudent(String courseId)throws Exception;
    public Future<List<AttendanceVO>> getAttendanceInfo(String courseId)throws Exception;
    public Future<Boolean> supplyAttendance(String studentId,String attendanceId)throws Exception;
    public void newCourse(CourseForm courseForm)throws Exception;
    public Future<Boolean> Sign(AttendanceForm attendanceForm)throws Exception;
    public Future<Boolean> Sign(String courseId,MultipartFile[] imageList)throws Exception;//上一个方法的改正
    public Future<CourseVO> searchByCourseId(String courseId, String teacherId)throws Exception;
    public Future<List<CourseVO>> searchByCourseName(String courseName,String teacherId)throws Exception;
    public Future<Boolean> updatePassword(String teacherId,String oldPassword,String newPassword)throws Exception;
    public Future<Boolean>  addAddress(String teacherId,String address)throws Exception;
    public Future<Boolean>  resetPassword(String teacherId,String password)throws Exception;
    public Future<String> getPassword(String id) throws Exception;
    public Future<String> getEmail(String id) throws Exception;

}
