package com.Attendance.student_sign_demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TeacherCourseVO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("stime")
    private Integer startTime;
    @JsonProperty("etime")
    private Integer endTime;
    @JsonProperty("days")
    private List<Integer> days;
    @JsonProperty("weeks")
    private List<Integer> weeks;
    @JsonProperty("semester")
    private String semester;
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

    public void setDays(List<Integer> courseDays) {
        this.days=courseDays;
    }

    public void setWeeks(List<Integer> courseWeeks) {
        this.weeks=courseWeeks;
    }

    public void setSemester(String courseSemester) {
        this.semester=courseSemester;
    }

}
