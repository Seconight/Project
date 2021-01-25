package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.Student;
import com.Attendance.student_sign_demo.entity.Teacher;
import com.Attendance.student_sign_demo.repository.StudentRepository;
import com.Attendance.student_sign_demo.repository.TeacherRepository;
import com.Attendance.student_sign_demo.service.StudentService;
import com.Attendance.student_sign_demo.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public LoginVO checkLogin(String id, String pwd) {
        LoginVO loginVO=new LoginVO();
        if(id.length()==9)
        {//老师登录
            Teacher teacher=teacherRepository.findByTeacherNo(id);
            if(pwd.equals(teacher.getTeacherPassword()))
            {
                loginVO.setUserNO(teacher.getTeacherNo());
                loginVO.setUserName(teacher.getTeacherName());
                loginVO.setUserClass(null);
                loginVO.setUserAddress(teacher.getTeacherAddress());
                loginVO.setRole("老师");
            }
        }
        else
        {//学生登录
            Student student=studentRepository.findByStudentNo(id);
            if(pwd.equals(student.getStudentPassword()))
            {
                loginVO.setUserNO(student.getStudentNo());
                loginVO.setUserName(student.getStudentName());
                loginVO.setUserClass(student.getStudentClass());
                loginVO.setUserAddress(student.getStudentAddress());
                loginVO.setRole("学生");
            }
        }
        return loginVO;
    }

    //人脸信息是否存在接口实现
    @Override
    public boolean checkFace(String id) {
        if(studentRepository.faceInfoIsNull(id)==null)
        return false;
        else return true;
    }
}
