package com.cfc.TXW.repository;

import com.cfc.TXW.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,String>{
    public Teacher findByTeacherNo(String teacherNo);
}
