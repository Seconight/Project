package com.Attendance.student_sign_demo.exception;

import com.Attendance.student_sign_demo.enums.ResultEnum;

public class StudentException extends RuntimeException{
    public StudentException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
    }

    public StudentException(String error){
        super(error);
    }
}
