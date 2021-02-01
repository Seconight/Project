package com.Attendance.student_sign_demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TeacherCourseVO {

    @JsonProperty("semester")
    private String semester;
    @JsonProperty("course")
    private List<TeacherCourse1VO> teacherCourse1VOList ;

    public void setSemester(String courseSemester) {
        this.semester=courseSemester;
    }
    public void setTeacherCourse1VOList(List<TeacherCourse1VO> teacherCourse1VOList){
        this.teacherCourse1VOList=teacherCourse1VOList;
    }

}
