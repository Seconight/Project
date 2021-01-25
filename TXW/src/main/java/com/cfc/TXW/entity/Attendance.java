package com.cfc.TXW.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

}
