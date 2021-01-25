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
<<<<<<< HEAD
    public String getAttendanceNo(){
        return this.attendanceNo;
    }
    public String getAttendanceTime(){
        return this.attendanceTime;
    }
    public String getAttendanceActualStudent()
    {
        return this.attendanceActualStudent;
    }
    public String getAttendanceAbsentStudent(){
        return this.attendanceAbsentStudent;
    }
    public void setAttendanceActualStudent(String s){
        this.attendanceActualStudent=s;
    }
    public void setAttendanceAbsentStudent(String s){
        this.attendanceAbsentStudent=s;
    }
=======

>>>>>>> e438378afe1848fcee7a0dfb2ef3832fb2082e47
}