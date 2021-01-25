package com.Attendance.student_sign_demo.service;

import com.Attendance.student_sign_demo.vo.CourseVO;
import com.Attendance.student_sign_demo.vo.LoginVO;
import com.Attendance.student_sign_demo.vo.StudentAttendanceVO;

import java.util.List;

public interface StudentService {
    public LoginVO checkLogin(String id, String pwd);
    public boolean checkFace(String id);
    public List<CourseVO> getCourses(String id);
    public List<StudentAttendanceVO> getStudentAttendance(String courseId,String studentId);
}
