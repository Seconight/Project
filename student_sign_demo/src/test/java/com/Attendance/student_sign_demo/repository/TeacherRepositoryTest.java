package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherRepositoryTest {
    @Autowired
    private TeacherRepository repository;
    @Test
    void findAll(){
        List<Teacher> list=repository.findAll();
        for(Teacher teacher : list){
            System.out.println(teacher);
        }
    }
    @Test
    void findByTeacherNo(){
        Teacher teacher=repository.findByTeacherNo("000000001");
        System.out.println(teacher);
    }
    @Test
    void save(){
        Teacher teacher=new Teacher();
        teacher.setTeacherNo("000000004");
        teacher.setTeacherAddress(null);
        teacher.setTeacherName("亮亮");
        teacher.setTeacherPassword("123456");
        repository.save(teacher);
    }

}