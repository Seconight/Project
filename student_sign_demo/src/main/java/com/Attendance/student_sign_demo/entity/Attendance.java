package com.Attendance.student_sign_demo.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Data
@Entity
public class Attendance {
    @Id
    @GeneratedValue
    private String attendanceNo;
    private String attendanceTime;
    private String attendanceActualStudent;
    private String attendanceAbsentStudent;
    private String attendanceCourseNo;
    public String getAttendanceTime(){
        return this.attendanceTime;
    }
    public String getAttendanceActualStudent()
    {
        return this.attendanceActualStudent;
    }
}