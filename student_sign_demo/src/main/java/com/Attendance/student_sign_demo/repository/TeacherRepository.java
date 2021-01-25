package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,String> {
    public  Teacher findByTeacherNo(String teacherNo);
}

