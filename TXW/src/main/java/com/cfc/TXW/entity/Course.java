package com.cfc.TXW.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Course {
    @Id
    private String courseNo;
    private String courseName;
    private String courseShouldStudent;
    private int courseStartTime;
    private int courseEndTime;
    private String courseDay;
    private String courseWeek;
    private String courseSemester;
    private String courseTeacherNo;

    public Course() {

    }
}
