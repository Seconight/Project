package com.Attendance.student_sign_demo.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Student {
    @Id
    private String studentNo;
    private String studentName;
    private String studentClass;
    private String studentAddress;
    private String studentEncoding;
    private String studentPassword;
    private String studentCourse;
    public String  getStudentNo(){
        return this.studentNo;
    }
    public String getStudentName(){
        return this.studentName;
    }
    public String getStudentClass(){
        return this.studentClass;
    }
    public String getStudentAddress(){
        return this.studentAddress;
    }
    public String getStudentEncoding(){
        return this.studentEncoding;
    }
    public String getStudentPassword(){
        return this.studentPassword;
    }
    public String[] getStudentCourse(){
        String[] courses=null;
        if(this.studentCourse!=null&&!this.studentCourse.equals(("")))
        {
            courses=this.studentCourse.split(",");
        }
        return courses;
    }
    public String getStudentCourses(){return this.studentCourse;}
    public void setStudentNo(String studentNo){
        this.studentNo=studentNo;
    }
    public void setStudentName(String studentName){
        this.studentName=studentName;
    }
    public void setStudentClass(String studentClass){
        this.studentClass=studentClass;
    }
    public void setStudentAddress(String studentAddress){
        this.studentAddress=studentAddress;
    }
    public void setStudentEncoding(String studentEncoding){
        this.studentEncoding=studentEncoding;
    }
    public void setStudentPassword(String studentPassword){
        this.studentPassword=studentPassword;
    }
    public void setStudentCourse(String courses){
        this.studentCourse=courses;
    }
    public void deleteStudentCourse(String courseNo){
        String deleteCourse=","+courseNo;//当前要删除的课号，课号间以，分割
        String[] courses=this.studentCourse.split(deleteCourse);
        this.studentCourse=courses[0];
        for(int i=1;i<courses.length;i++){
            this.studentCourse=this.studentCourse+courses[i];
        }
    }
}