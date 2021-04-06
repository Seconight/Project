package com.Attendance.student_sign_demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AttendanceVO {
    @JsonProperty("attendanceId")
    private String attendanceId;
    @JsonProperty("time")
    private String time;
    @JsonProperty("acStudentNum")
    private Integer acStudentNum;
    @JsonProperty("acStudent")
    private List<CourseStudentVO> acStudent;
    @JsonProperty("abStudentNum")
    private Integer abStudentNum;
    @JsonProperty("abStudent")
    private List<CourseStudentVO> abStudent;

    public void setAttendanceId(String id)
    {
        this.attendanceId=id;
    }

    public void setTime(String attendanceTime) {
        this.time=attendanceTime;
    }

    public void setAcStudentNum(int i) {
        this.acStudentNum=i;
    }

    public void setAcStudent(List<CourseStudentVO> courseStudentVOList) {
        this.acStudent=courseStudentVOList;
    }

    public void setAbStudentNum(int i) {
        this.abStudentNum=i;
    }

    public void setAbStudent(List<CourseStudentVO> courseStudentVOList) {
        this.abStudent=courseStudentVOList;
    }

    public String getTime() {
        return time;
    }
}
