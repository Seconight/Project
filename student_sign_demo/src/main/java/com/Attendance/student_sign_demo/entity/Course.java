package com.Attendance.student_sign_demo.entity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

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

    public String getCourseNo(){
        return this.courseNo;
    }
    public String getCourseName(){
        return this.courseName;
    }
    public Integer getCourseStartTime(){
        return this.courseStartTime;
    }
    public Integer getCourseEndTime(){
        return this.courseEndTime;
    }
    public String [] getCourseShouldStudent(){
        String[] students=null;
        if(this.courseShouldStudent!=null||!this.courseShouldStudent.equals(""))
        {
            students=this.courseShouldStudent.split(",");
        }
        return students;
    }
    public List<Integer> getCourseDays(){
        String[] days=null;
        List<Integer> dayList=new ArrayList<Integer>();
        if(this.courseDay!=null)
        {
            days=this.courseDay.split(",");
            for(int i=0;i<days.length;i++){
                dayList.add(Integer.parseInt(days[i]));
            }
        }
        return dayList;
    }
    public List<Integer> getCourseWeeks(){
        String[] days=null;
        List<Integer> dayList=new ArrayList<Integer>();
        if(this.courseWeek!=null)
        {
            days=this.courseWeek.split(",");
            for(int i=0;i<days.length;i++){
                dayList.add(Integer.parseInt(days[i]));
            }
        }
        return dayList;
    }
    public String getCourseSemester(){
        return this.courseSemester;
    }
    public String getCourseTeacherNo() {
        return this.courseTeacherNo;
    }

    public void setCourseNo(String courseNo){
        this.courseNo=courseNo;
    }
    public void setCourseName(String courseName){
        this.courseName=courseName;
    }
    public void setCourseShouldStudent(String s){
        this.courseShouldStudent=s;
    }
    public void setCourseStartTime(Integer t){
        this.courseStartTime=t;
    }
    public void setCourseEndTime(Integer t){
        this.courseEndTime=t;
    }
    public void setCourseDay(String days){
        this.courseDay=days;
    }
    public void setCourseWeek(String weeks){
        this.courseWeek=weeks;
    }
    public void setCourseSemester(String semester){
        this.courseSemester=semester;
    }
    public void setCourseTeacherNo(String teacherNo){
        this.courseTeacherNo=teacherNo;
    };
    public Course() {

    }
}