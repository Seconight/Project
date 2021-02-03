package com.Attendance.student_sign_demo.form;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;


@Data
public class AttendanceForm {
    @NotEmpty(message = "文件不能为空！")
    private MultipartFile img;
    @NotEmpty(message = "课程ID不能为空！")
    private String id;

    public String getId() {
        return id;
    }

    public MultipartFile getImg() {
        return img;
    }
}
