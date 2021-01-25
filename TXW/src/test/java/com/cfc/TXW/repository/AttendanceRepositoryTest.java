package com.cfc.TXW.repository;

import com.cfc.TXW.entity.Attendance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AttendanceRepositoryTest {
    @Autowired
    private AttendanceRepository repository;
    @Test
    void findAll(){
        List<Attendance> list=repository.findAll();
        for(Attendance attendance:list) {
            System.out.println(attendance);
        }
    }
    @Test
    void findByAttendanceNo(){
        Attendance attendance=repository.findByAttendanceNo("0000000001");
        System.out.println(attendance);
    }
}