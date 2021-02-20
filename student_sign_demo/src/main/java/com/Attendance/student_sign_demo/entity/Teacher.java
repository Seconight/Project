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
    public String getTeacherCourses(){return this.teacherCourse;}
    public String[] getTeacherCourse(){
        String[] courses=null;
        if(this.teacherCourse!=null)
        {
            courses=this.teacherCourse.split(",");
        }
        return courses;
    }
    public void setTeacherNo(String teacherNo){
        this.teacherNo=teacherNo;
    }
    public void setTeacherName(String teacherName){
        this.teacherName=teacherName;
    }
    public void setTeacherAddress(String teacherAddress){
        this.teacherAddress=teacherAddress;
    }
    public void setTeacherPassword(String password){
        this.teacherPassword=password;
    }
    public void setTeacherCourse(String course){
        this.teacherCourse=course;
    }
}