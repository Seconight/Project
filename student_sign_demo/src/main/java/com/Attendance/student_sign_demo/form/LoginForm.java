package com.Attendance.student_sign_demo.form;

import lombok.Data;
import javax.validation.constraints.NotEmpty;


//注意！此处参数输入格式为JSON，且JSON中的字段必须与类中的名称相同
@Data
public class LoginForm {
    @NotEmpty(message = "用户id不可为空")
    private String id;
    @NotEmpty(message = "用户密码不可为空")
    private String pwd;

    public LoginForm(){}
    public LoginForm(String id, String password){
        this.id = id;
        this.pwd = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.pwd = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return pwd;
    }
}
