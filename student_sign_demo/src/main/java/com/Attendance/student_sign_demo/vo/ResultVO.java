package com.Attendance.student_sign_demo.vo;

import lombok.Data;

@Data
public class ResultVO<T> {
    private Integer code;
    private String message;
    private T data;

    public ResultVO(){

    }

    public ResultVO(Integer code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
