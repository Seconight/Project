package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.Attendance;
import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.entity.Student;
import com.Attendance.student_sign_demo.repository.AttendanceRepository;
import com.Attendance.student_sign_demo.repository.CourseRepository;
import com.Attendance.student_sign_demo.repository.StudentRepository;
import com.Attendance.student_sign_demo.repository.TeacherRepository;
import com.Attendance.student_sign_demo.service.TeacherService;
import com.Attendance.student_sign_demo.vo.AttendanceVO;
import com.Attendance.student_sign_demo.vo.CourseStudentVO;
import com.Attendance.student_sign_demo.vo.TeacherCourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Override
    public List<TeacherCourseVO> getCourses(String id) {
        String[] courses=teacherRepository.findByTeacherNo(id).getTeacherCourse();
        List<TeacherCourseVO> teacherCourseVOList=new ArrayList<>();
        for(int i=0;i<courses.length;i++)
        {
            TeacherCourseVO teacherCourseVO=new TeacherCourseVO();
            Course course=courseRepository.findByCourseNo(courses[i]);
            teacherCourseVO.setId(course.getCourseNo());
            teacherCourseVO.setName(course.getCourseName());
            teacherCourseVO.setStartTime(course.getCourseStartTime());
            teacherCourseVO.setEndTime(course.getCourseEndTime());
            teacherCourseVO.setDays(course.getCourseDays());
            teacherCourseVO.setWeeks(course.getCourseWeeks());
            teacherCourseVO.setSemester(course.getCourseSemester());
            teacherCourseVOList.add(teacherCourseVO);
        }
        return teacherCourseVOList;
    }

    @Override
    public List<CourseStudentVO> getCourseStudent(String courseId) {
        List<CourseStudentVO> courseStudentVOList=new ArrayList<>();
        String[] studentId=courseRepository.findByCourseNo(courseId).getCourseShouldStudent();
        for(int i=0;i<studentId.length;i++)
        {
            Student student=studentRepository.findByStudentNo(studentId[i]);
            CourseStudentVO courseStudentVO=new CourseStudentVO();
            courseStudentVO.setStudentNo(student.getStudentNo());
            courseStudentVO.setStudentClass(student.getStudentClass());
            courseStudentVO.setStudentName(student.getStudentName());
            courseStudentVOList.add(courseStudentVO);
        }
        return courseStudentVOList;
    }

    @Override
    public List<AttendanceVO> getAttendanceInfo(String courseId) {
        List<AttendanceVO> attendanceVOList=new ArrayList<>();
        List<Attendance> attendanceList=attendanceRepository.findByAttendanceCourseNo(courseId);
        for(Attendance attendance:attendanceList){
            AttendanceVO attendanceVO=new AttendanceVO();
            attendanceVO.setAttendanceId(attendance.getAttendanceNo());
            attendanceVO.setTime(attendance.getAttendanceTime());
            String[] acStudents=attendance.getAttendanceActualStudent().split(",");
            attendanceVO.setAcStudentNum(acStudents.length);
            List<CourseStudentVO> courseStudentVOList=new ArrayList<>();
            for(int i=0;i<acStudents.length;i++)
            {
                CourseStudentVO courseStudentVO=new CourseStudentVO();
                Student student=studentRepository.findByStudentNo(acStudents[i]);
                courseStudentVO.setStudentNo(acStudents[i]);
                courseStudentVO.setStudentName(student.getStudentName());
                courseStudentVO.setStudentClass(student.getStudentClass());
                courseStudentVOList.add(courseStudentVO);
            }
            attendanceVO.setAcStudent(courseStudentVOList);
            String[] abStudents=attendance.getAttendanceAbsentStudent().split(",");
            attendanceVO.setAbStudentNum(abStudents.length);
            courseStudentVOList=new ArrayList<CourseStudentVO>();
            for(int i=0;i<abStudents.length;i++)
            {
                CourseStudentVO courseStudentVO=new CourseStudentVO();
                Student student=studentRepository.findByStudentNo(abStudents[i]);
                courseStudentVO.setStudentNo(abStudents[i]);
                courseStudentVO.setStudentName(student.getStudentName());
                courseStudentVO.setStudentClass(student.getStudentClass());
                courseStudentVOList.add(courseStudentVO);
            }
            attendanceVO.setAbStudent(courseStudentVOList);
            attendanceVOList.add(attendanceVO);
        }
        return attendanceVOList;
    }

    @Override
    public boolean supplyAttendance(String studentId,String attendanceId) {
        Attendance attendance=attendanceRepository.findByAttendanceNo(attendanceId);
        String actualStudent=attendance.getAttendanceActualStudent();
        if(actualStudent!=null&&actualStudent.length()!=0){
            attendance.setAttendanceActualStudent(actualStudent+","+studentId);
        }
        else{
            attendance.setAttendanceActualStudent(studentId);
        }
        String absentStudent=attendance.getAttendanceAbsentStudent();
        if(absentStudent.length()==13)
        {
            attendance.setAttendanceAbsentStudent("");
        }
        else{
            attendance.setAttendanceAbsentStudent(absentStudent.replace(","+studentId,""));
        }
        attendanceRepository.save(attendance);
        return true;
    }
}
