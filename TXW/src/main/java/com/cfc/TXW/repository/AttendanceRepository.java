package com.cfc.TXW.repository;

import com.cfc.TXW.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,String> {
    public Attendance findByAttendanceNo(String attendanceNo);
}
