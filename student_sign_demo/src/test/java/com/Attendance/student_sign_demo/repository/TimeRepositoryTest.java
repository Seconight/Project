package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.StartTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TimeRepositoryTest {
    @Autowired
    private TimeRepository timeRepository;
    @Test
    void findAll(){
        List<StartTime> startTimes=timeRepository.findAll();
        System.out.println(startTimes.get(0));
    }
}