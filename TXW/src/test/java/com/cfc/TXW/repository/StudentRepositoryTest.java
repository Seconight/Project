package com.cfc.TXW.repository;

import com.cfc.TXW.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StudentRepositoryTest {
    @Autowired
    private StudentRepository repository;
    @Test
    void findAll(){
        List<Student> list=repository.findAll();
        for(Student student : list){
            System.out.println(student);
        }
    }
    @Test
    void findByStudentNo(){
        Student student=repository.findByStudentNo("0121810880214");
        System.out.println(student);
    }
}