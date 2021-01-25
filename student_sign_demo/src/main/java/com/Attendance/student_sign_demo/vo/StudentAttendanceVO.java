package com.Attendance.student_sign_demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StudentAttendanceVO {
    @JsonProperty("time")
    private String time;
    @JsonProperty("success")
    private boolean success;
    public void setTime(String time)
    {
        this.time=time;
    }
    public void setSuccess(boolean success){
        this.success=success;
    }
}
