package com.cfc.TXW.repository;

import com.cfc.TXW.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,String> {
    public Student findByStudentNo(String studentNo);
}
