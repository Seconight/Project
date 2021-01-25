package com.Attendance.student_sign_demo.service;

import com.Attendance.student_sign_demo.vo.AttendanceVO;
import com.Attendance.student_sign_demo.vo.CourseStudentVO;
import com.Attendance.student_sign_demo.vo.TeacherCourseVO;

import java.util.List;

public interface TeacherService {
    public List<TeacherCourseVO> getCourses(String teacherId);
    public List<CourseStudentVO> getCourseStudent(String courseId);
    public List<AttendanceVO> getAttendanceInfo(String courseId);
    public boolean supplyAttendance(String studentId,String attendanceId);
}
