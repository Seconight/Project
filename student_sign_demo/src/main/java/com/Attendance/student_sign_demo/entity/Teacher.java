package com.Attendance.student_sign_demo.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Teacher {
    @Id
    @GeneratedValue
    private String teacherNo;
    private String teacherName;
    private String teacherAddress;
    private String teacherCourse;
    private String teacherPassword;

    public String getTeacherNo() {
        return this.teacherNo;
    }

    public String getTeacherName() {
        return this.teacherName;
    }

    public String getTeacherAddress() {
        return this.teacherAddress;
    }

    public String getTeacherPassword() {
        return this.teacherPassword;
    }
}