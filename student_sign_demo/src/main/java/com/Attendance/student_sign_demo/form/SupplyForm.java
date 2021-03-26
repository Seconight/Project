package com.Attendance.student_sign_demo.form;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SupplyForm {

    @NotEmpty(message = "学生Id列表不能为空")
    private String[] studentIds;
    @NotEmpty(message = "签到记录id不能为空")
    private String attendanceId;

    public String[] getStudentIds() {
        return studentIds;
    }

    public String getAttendanceId() {
        return attendanceId;
    }
}
