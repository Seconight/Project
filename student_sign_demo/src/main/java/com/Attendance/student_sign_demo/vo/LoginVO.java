package com.Attendance.student_sign_demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginVO {
    @JsonProperty("id")
    private String userNo;
    @JsonProperty("name")
    private String userName;
    @JsonProperty("class")
    private String userClass;
    @JsonProperty("address")
    private String userAddress;
    @JsonProperty("role")
    private String role;


    public void setUserNO(String No) {
        this.userNo=No;
    }

    public void setUserName(String Name) {
        this.userName=Name;
    }

    public void setUserClass(String Class) {
        this.userClass=Class;
    }

    public void setUserAddress(String Address) {
        this.userAddress=Address;
    }

    public void setRole(String s) {
        this.role=s;
    }

    public String getUserNO() {return this.userNo;}
}
