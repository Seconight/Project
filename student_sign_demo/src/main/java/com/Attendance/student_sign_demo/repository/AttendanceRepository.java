package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,String> {
    public List<Attendance> findByAttendanceCourseNo(String attendanceCourseNo);
}
