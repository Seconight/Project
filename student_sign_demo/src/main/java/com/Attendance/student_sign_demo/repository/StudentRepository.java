package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,String> {
    //根据学生学号获取学生对象
    public Student findByStudentNo(String studentNo);
    //根据班级获得该班所有学生
    public List<Student> findAllByStudentClass(String studentClass);
    //根据学生学号判断学生是否有特征向量
    @Query("select t.studentEncoding from Student t where t.studentNo = ?1")
    public String faceInfoIsNull(String studentNo);
}
