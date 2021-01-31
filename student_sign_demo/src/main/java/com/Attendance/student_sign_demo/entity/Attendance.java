package com.Attendance.student_sign_demo.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Data
@Entity
public class Attendance {
    @Id
    private String attendanceNo;
    private String attendanceTime;
    private String attendanceActualStudent;
    private String attendanceAbsentStudent;
    private String attendanceCourseNo;

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
    public String getAttendanceCourseNo() {
        return attendanceCourseNo;
    }

    public void setAttendanceActualStudent(String s){
        this.attendanceActualStudent=s;
    }
    public void setAttendanceAbsentStudent(String s){
        this.attendanceAbsentStudent=s;
    }


    public void setAttendanceNo(String attendanceId) {
        this.attendanceNo=attendanceId;
    }

    public void setAttendanceTime(String s) {
        this.attendanceTime=s;
    }

    public void setAttendanceCourseNo(String courseId) {
        this.attendanceCourseNo=courseId;
    }
}