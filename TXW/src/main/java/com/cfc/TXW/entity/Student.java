package com.cfc.TXW.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String studentNo;
    private String studentName;
    private String studentClass;
    private String studentAddress;
    private String studentEncoding;
    private String studentPassword;
    private String studentCourse;
}
