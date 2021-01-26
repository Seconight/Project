package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.StartTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRepository extends JpaRepository<StartTime,String> {
}
