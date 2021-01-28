package com.Attendance.student_sign_demo.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class CourseForm {

    @NotEmpty(message = "课程名称为空！")
    private String name;
    @NotEmpty(message = "开始时间为空！")
    private Integer sTime;
    @NotEmpty(message = "结束时间为空！")
    private Integer eTime;
    @NotEmpty(message = "工作日数为空！")
    private String day;
    @NotEmpty(message = "周数为空！")
    private String week;
    @NotEmpty(message = "学期数名称为空！")
    private String semester;
    @NotEmpty(message = "老师工号为空！")
    private String teacherId;
    @NotEmpty(message = "选课学生文件为空！")
    private MultipartFile students;

    public void setName(String name) {
        this.name = name;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void seteTime(Integer eTime) {
        this.eTime = eTime;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setsTime(@NotEmpty(message = "开始时间为空！") Integer sTime) {
        this.sTime = sTime;
    }

    public void setStudents(MultipartFile students) {
        this.students = students;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getName() {
        return name;
    }

    public MultipartFile getStudents() {
        return students;
    }

    public String getDay() {
        return day;
    }

    public Integer geteTime() {
        return eTime;
    }

    public String getSemester() {
        return semester;
    }

    public @NotEmpty(message = "开始时间为空！") Integer getsTime() {
        return sTime;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getWeek() {
        return week;
    }


}
