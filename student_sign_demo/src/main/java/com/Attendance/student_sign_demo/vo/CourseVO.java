package com.Attendance.student_sign_demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

//课程视图对象
@Data
public class CourseVO {
    @JsonProperty("semester")
    private String semester;
    @JsonProperty("course")
    private List<Course1VO> course1VOList;


    public void setSemester(String courseSemester) {
        this.semester = courseSemester;
    }
    public void setCourse1VOList(List<Course1VO> course1VOList){
        this.course1VOList=course1VOList;
    }
}
