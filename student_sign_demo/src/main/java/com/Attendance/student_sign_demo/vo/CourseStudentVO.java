package com.Attendance.student_sign_demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
//视图JSON:对应课程学生名单
@Data
public class CourseStudentVO {
    @JsonProperty("id")
    private String studentNo;
    @JsonProperty("name")
    private String studentName;
    @JsonProperty("class")
    private String studentClass;
    public void setStudentNo(String studentNo){
        this.studentNo=studentNo;
    }
    public void setStudentClass(String studentClass)
    {
        this.studentClass=studentClass;
    }
    public void setStudentName(String studentName)
    {
        this.studentName=studentName;
    }
}
