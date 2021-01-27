package com.Attendance.student_sign_demo.enums;

import lombok.Getter;

//枚举类，用于返回对应操作的信息，待完善
@Getter
public enum ResultEnum{
    //example
    STUDENT_NOT_FOUND_ERROR(0,"未找到学生");


    private Integer code;
    private String message;

    ResultEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
