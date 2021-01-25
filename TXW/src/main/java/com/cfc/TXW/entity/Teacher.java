package com.cfc.TXW.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Teacher {
    @Id
    private String teacherNo;
    private String teacherName;
    private String teacherAddress;
    private String teacherCourse;
    private String teacherPassword;
}
