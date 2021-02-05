package com.Attendance.student_sign_demo;

import com.Attendance.student_sign_demo.service.impl.StudentServiceImpl;
import com.Attendance.student_sign_demo.service.impl.TeacherServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
//@ComponentScan(basePackages = "com.Attendance.student_sign_demo.service.impl")
public class StudentSignDemoApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context=SpringApplication.run(StudentSignDemoApplication.class, args);
	}
}
