package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Attendance;
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
<<<<<<< HEAD
        System.out.println(attendance);
    }
    @Test
    void findByAttendanceCourseNo(){
        List<Attendance> attendance=repository.findByAttendanceCourseNo("0000000001");
=======
>>>>>>> e438378afe1848fcee7a0dfb2ef3832fb2082e47
        System.out.println(attendance);
    }
}