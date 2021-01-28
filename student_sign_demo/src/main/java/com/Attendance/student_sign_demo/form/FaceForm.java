package com.Attendance.student_sign_demo.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class FaceForm {
    @NotEmpty(message = "人脸信息不能为空")
    private MultipartFile faceImage;
    @NotEmpty(message = "学生ID不能为空")
    private String studentId;

    public MultipartFile getFaceImage() {
        return faceImage;
    }

    public String getStudentId() {
        return studentId;
    }
}
