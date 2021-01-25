package com.Attendance.student_sign_demo.repository;

import com.Attendance.student_sign_demo.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,String> {
<<<<<<< HEAD
    public List<Attendance> findByAttendanceCourseNo(String attendanceCourseNo);
=======
>>>>>>> e438378afe1848fcee7a0dfb2ef3832fb2082e47
    public Attendance findByAttendanceNo(String attendanceNo);
}
