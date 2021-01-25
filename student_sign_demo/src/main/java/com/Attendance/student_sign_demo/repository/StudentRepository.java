package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,String> {
    public Student findByStudentNo(String studentNo);
    public List<Student> findAllByStudentClass(String studentClass);
}
