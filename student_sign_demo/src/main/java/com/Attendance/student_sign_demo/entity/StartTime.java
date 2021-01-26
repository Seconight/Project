package com.Attendance.student_sign_demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class StartTime {
    @Id
    private String up;
    private String down;
    public String getUp(){
        return this.up;
    }
    public String getDown(){
        return this.down;
    }
}
