package com.cfc.TXW.repository;

import com.cfc.TXW.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CourseRepositoryTest {
    @Autowired
    private CourseRepository repository;
    @Test
    void findAll(){
        List<Course> list= repository.findAll();
        for(Course course:list){
            System.out.println(course);
        }
    }
    @Test
    void findByCourseNo(){
        Course course=repository.findByCourseNo("0000000001");
        System.out.println(course);
    }
}