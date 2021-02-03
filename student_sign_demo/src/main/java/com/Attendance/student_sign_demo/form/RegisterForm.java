package com.Attendance.student_sign_demo.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegisterForm {

    @NotEmpty(message = "id不能为空")
    private String id;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotEmpty(message = "姓名不能为空")
    private String name;
    private String studentClass;
    private String address;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getStudentClass() {
        return studentClass;
    }
}
