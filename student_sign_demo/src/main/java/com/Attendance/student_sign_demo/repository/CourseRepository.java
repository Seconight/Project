package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,String> {
    public Course findByCourseNo(String courseNo);
}