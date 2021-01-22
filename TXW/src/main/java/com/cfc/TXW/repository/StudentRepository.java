package com.cfc.TXW.repository;

import com.cfc.TXW.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,String> {
    public Student findByStudentNo(String studentNo);
    public List<Student> findAllByStudentClass(String studentClass);
    public Student save(Student student);
}
