package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,String> {
    public Attendance findByAttendanceNo(String attendanceNo);
}
