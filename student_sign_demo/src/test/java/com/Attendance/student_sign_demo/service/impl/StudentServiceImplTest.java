package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.entity.Student;
import com.Attendance.student_sign_demo.form.LoginForm;
import com.Attendance.student_sign_demo.grpc.ClientService;
import com.Attendance.student_sign_demo.repository.StudentRepository;
import com.Attendance.student_sign_demo.service.StudentService;
import com.Attendance.student_sign_demo.util.PathUtil;
import com.Attendance.student_sign_demo.vo.CourseVO;
import com.Attendance.student_sign_demo.vo.LoginVO;
import com.Attendance.student_sign_demo.vo.StudentAttendanceVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Access;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

//@EnableTransactionManagement(proxyTargetClass = true)
@RunWith(SpringRunner.class)
@SpringBootTest
class StudentServiceImplTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;


    @Test
    void checkLogin() throws Exception {
        LoginForm loginForm = new LoginForm("0121810880214","123456");
        Future<LoginVO> loginVOFuture=studentService.checkLogin(loginForm);
        LoginVO loginVO=new LoginVO();
        while(true){
            if(loginVOFuture.isDone()){
                loginVO=loginVOFuture.get();
                break;
            }
        }
        int a=0;
    }
    @Test
    void checkFaceInfo() throws Exception {
        Future<Boolean> booleanFuture=studentService.checkFace("0121810880214");
        Boolean result =false;
        while(true){
            if(booleanFuture.isDone()){
                result=booleanFuture.get();
                break;
            }
        }
        int a=0;
    }
    @Test
    void getCourses() throws Exception {
    Future<List<CourseVO>> courseVOFuture=studentService.getCourses("0121810880207");
    List<CourseVO> courseVOList=new ArrayList<>();
    while(true){
        if(courseVOFuture.isDone()){
            courseVOList=courseVOFuture.get();
            break;
        }
    }
    for(CourseVO courseVO:courseVOList){
        System.out.println(courseVO);
    }
     }
    @Test
    void getStudentAttendance() throws Exception {
    Future<List<StudentAttendanceVO>> studentAttendanceVOFuture=studentService.getStudentAttendance("0000000001","0121810880214");
    List<StudentAttendanceVO> studentAttendanceVOList=new ArrayList<>();
    while(true){
        if(studentAttendanceVOFuture.isDone()){
            studentAttendanceVOList=studentAttendanceVOFuture.get();
            break;
        }
    }
    int a=0;
     }
    @Test
    void register() throws Exception {
        //studentService.register("000000004","123456",null,null,"亮亮");
    }
    @Test
    void searchByCourseId() throws Exception {
        Future<CourseVO> courseVOFuture=studentService.searchByCourseId("0000000002","0121810880201");
        CourseVO courseVO=new CourseVO();
        while(true){
            if(courseVOFuture.isDone())
            {
                courseVO=courseVOFuture.get();
                break;
            }
        }
        System.out.println(courseVO);
        int a=0;
    }
    @Test
    void searchByCourseName() throws Exception {
        Future<List<CourseVO>> courseVOFuture=studentService.searchByCourseName("C","0121810880201");
        List<CourseVO> courseVOList=new ArrayList<>();
        while(true){
            if(courseVOFuture.isDone()){
                courseVOList=courseVOFuture.get();
                break;
            }
        }
        for(CourseVO courseVO:courseVOList){
            System.out.println(courseVO);
        }
        int a=0;
    }
    @Test
    void getStudentFace()throws Exception{
//        Future<List<MultipartFile>> multipartFiles=studentService.getStudentFace("0121810880214");
//        List<MultipartFile> multipartFiles1=new ArrayList<>();
//        while(true){
//            if(multipartFiles.isDone()){
//                multipartFiles1=multipartFiles.get();
//                break;
//            }
//        }
//        if(!multipartFiles1.isEmpty())
//        {
//            int i=0;
//            for(MultipartFile multipartFile:multipartFiles1){
//                multipartFile.transferTo(new File("E:\\360Downloads\\计算机设计大赛\\学生人脸\\0121810880214"+String.valueOf(i)+".jpg"));
//                System.out.println("成功");
//            }
//        }
//        else{
//            System.out.println("无");
//        }
    }
    @Test
    void searchByCourseNameForAdd()throws Exception{
        Future<List<CourseVO>> courseVOFuture=studentService.searchByCourseNameForAdd("C","0121810880201");
        List<CourseVO> courseVOList=new ArrayList<>();
        while(true){
            if(courseVOFuture.isDone()){
                courseVOList=courseVOFuture.get();
                break;
            }
        }
        for(CourseVO courseVO:courseVOList){
            System.out.println(courseVO);
        }
    }
    @Test
    void searchByCourseIdForAdd() throws Exception{
        Future<CourseVO> courseVOFuture=studentService.searchByCourseIdForAdd("0121810880201","0000000005");
        CourseVO courseVO=new CourseVO();
        while(true){
            if(courseVOFuture.isDone())
            {
                courseVO=courseVOFuture.get();
                break;
            }
        }
        System.out.println(courseVO);
    }

    @Test
    void addStudentFaces() throws Exception{

        for(int i=20;i<=20;i++){
            String studentNo = "01218108807";
            if(i<10) studentNo += "0";
            studentNo += String.valueOf(i);
            //保存编码
            String encodings="";
            //grpc获取服务结果
            ClientService clientService=new ClientService();
            encodings=clientService.FaceRecognize(studentNo);

            //写入数据库
            Student student=studentRepository.findByStudentNo(studentNo);
            student.setStudentEncoding(encodings.substring(0,encodings.length()-1));
            studentRepository.save(student);//更新

        }

    }
    @Test
    void testLength()throws Exception{
        List<Student> students=studentRepository.findAll();
        StringBuilder length= new StringBuilder();
        for(int i=0;i<students.size();i++){
            length.append(students.get(i).getStudentEncoding());
            System.out.println(length.length());
        }
        String newLength=length.toString();
        System.out.println(newLength);
    }
}