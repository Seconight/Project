package com.cfc.TXW.repository;

import com.cfc.TXW.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,String> {
    public Course findByCourseNo(String courseNo);
}
