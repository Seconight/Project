package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Student;
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
    @Test
    void findAllByStudentClass(){
        List<Student> list=repository.findAllByStudentClass("计算机1801");
        for(Student student:list){
            System.out.println(student);
        }
    }
    @Test
    void save(){
        Student student=new Student();
        student.setStudentNo("0121810880201");
        student.setStudentName("罗文平");
        student.setStudentClass("计算机1802");
        student.setStudentPassword("123456");
        repository.save(student);
    }
    @Test
    void update(){
        Student student=repository.findByStudentNo("0121810880204");
        student.setStudentPassword("111111");
        repository.save(student);
    }
    @Test
    void checkFace(){
        System.out.println("this is "+repository.faceInfoIsNull("0121810880207"));
    }
}