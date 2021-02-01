package com.Attendance.student_sign_demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class TeacherCourse1VO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("stime")
    private Integer startTime;
    @JsonProperty("etime")
    private Integer endTime;
    @JsonProperty("studentNum")
    private Integer studentNum;
    @JsonProperty("days")
    private List<Integer> days;
    @JsonProperty("weeks")
    private List<Integer> weeks;
    public void setId(String courseNo) {
        this.id=courseNo;
    }

    public void setName(String courseName) {
        this.name=courseName;
    }

    public void setStartTime(Integer courseStartTime) {
        this.startTime=courseStartTime;
    }

    public void setEndTime(Integer courseEndTime) {
        this.endTime=courseEndTime;
    }

    public void setStudentNum(Integer studentNum){
        this.studentNum=studentNum;
    }

    public void setDays(List<Integer> courseDays) {
        this.days=courseDays;
    }

    public void setWeeks(List<Integer> courseWeeks) {
        this.weeks=courseWeeks;
    }
}
