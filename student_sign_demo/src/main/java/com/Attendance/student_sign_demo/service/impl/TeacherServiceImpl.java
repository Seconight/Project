package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.Attendance;
import com.Attendance.student_sign_demo.entity.Course;
import com.Attendance.student_sign_demo.entity.StartTime;
import com.Attendance.student_sign_demo.entity.Student;
import com.Attendance.student_sign_demo.repository.*;
import com.Attendance.student_sign_demo.service.TeacherService;
import com.Attendance.student_sign_demo.util.TimeUtil;
import com.Attendance.student_sign_demo.vo.AttendanceVO;
import com.Attendance.student_sign_demo.vo.CourseStudentVO;
import com.Attendance.student_sign_demo.vo.TeacherCourseVO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Supplier;

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
    @Autowired
    private TimeRepository timeRepository;
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
            if(acStudents.equals(""))
            {
                attendanceVO.setAcStudentNum(0);
            }
            else{
                attendanceVO.setAcStudentNum(acStudents.length);
            }
            List<CourseStudentVO> courseStudentVOList=new ArrayList<>();
            for(int i=0;i<acStudents.length;i++)
            {
                CourseStudentVO courseStudentVO=new CourseStudentVO();
                Student student=studentRepository.findByStudentNo(acStudents[i]);
                if(student!=null){
                    courseStudentVO.setStudentNo(acStudents[i]);
                    courseStudentVO.setStudentName(student.getStudentName());
                    courseStudentVO.setStudentClass(student.getStudentClass());
                    courseStudentVOList.add(courseStudentVO);
                }
            }
            attendanceVO.setAcStudent(courseStudentVOList);
            String[] abStudents=attendance.getAttendanceAbsentStudent().split(",");
            if(abStudents[0].equals("")){
                attendanceVO.setAbStudentNum(0);
            }
            else{
                attendanceVO.setAbStudentNum(abStudents.length);
            }
            courseStudentVOList=new ArrayList<CourseStudentVO>();
            for(int i=0;i<abStudents.length;i++)
            {
                CourseStudentVO courseStudentVO=new CourseStudentVO();
                Student student=studentRepository.findByStudentNo(abStudents[i]);
                if(student!=null) {
                    courseStudentVO.setStudentNo(abStudents[i]);
                    courseStudentVO.setStudentName(student.getStudentName());
                    courseStudentVO.setStudentClass(student.getStudentClass());
                    courseStudentVOList.add(courseStudentVO);
                }
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

    @Override
    public void newCourse(String courseName, Integer startTime, Integer endTime, String days, String weeks, String semester, String teacherId, MultipartFile studentsFile) {
        Integer num=courseRepository.findAll().toArray().length+1;
        String number=String.valueOf(num);
        String courseId="";
        for(int i=0;i<10-number.length();i++){
            courseId=courseId+"0";
        }
        courseId=courseId+number;
        Course newCourse=new Course();
        newCourse.setCourseNo(courseId);
        newCourse.setCourseName(courseName);
        newCourse.setCourseStartTime(startTime);
        newCourse.setCourseEndTime((endTime));
        newCourse.setCourseDay(days);
        newCourse.setCourseWeek(weeks);
        newCourse.setCourseSemester(semester);
        newCourse.setCourseTeacherNo(teacherId);
        String courseStudents="";
       try{
           HSSFWorkbook workbook=new HSSFWorkbook(studentsFile.getInputStream());
           HSSFSheet hssfSheet=workbook.getSheetAt(0);//获取表单
           int numOfRows=hssfSheet.getPhysicalNumberOfRows();//获取行数
           for(int i=1;i<numOfRows;i++)
           {
               HSSFRow hssfRow=hssfSheet.getRow(i);//获取行
               if(hssfRow==null){
                   continue;//空行跳过
               }
               HSSFCell hssfCell=hssfRow.getCell(0);//第二行一列数据
               String studentNo=hssfCell.getStringCellValue();//获得学生学号
               courseStudents=courseStudents+studentNo+",";
           }
       }catch (IOException e){
           e.printStackTrace();;
       }
       courseStudents=courseStudents.substring(0,courseStudents.length()-1);
       newCourse.setCourseShouldStudent(courseStudents);
       courseRepository.save(newCourse);
    }

    @Override
    public void Sign(MultipartFile imageFile,String courseId) {
        //先将文件存到本地
        String filePath="E:/360Downloads/计算机设计大赛/cdc_face/keras-face-recognition-master/attendance.jpg"; //定义上传文件的存放位置
        //将传来的图片保存到本地
        try{
            imageFile.transferTo(new File(filePath));
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        //根据课程号把学生名单以及对应的人脸encoding保存到文件中
        String [] studentsId=courseRepository.findByCourseNo(courseId).getCourseShouldStudent();
        String studentAndEncoding="";
        for(int i=0;i<studentsId.length-1;i++){
            Student student=studentRepository.findByStudentNo(studentsId[i]);
            studentAndEncoding=studentAndEncoding+studentsId[i]+":"+student.getStudentEncoding()+";";
        }
        Student student=studentRepository.findByStudentNo(studentsId[studentsId.length-1]);
        studentAndEncoding=studentAndEncoding+studentsId[studentsId.length-1]+":"+student.getStudentEncoding();
        try{
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(new File("E:/360Downloads/计算机设计大赛/cdc_face/keras-face-recognition-master/shouldStudents.txt")));
            bufferedWriter.write(studentAndEncoding);
            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        //进行人脸识别将识别出的人脸保存到文件里
        String actualStudentFilePath="E:/360Downloads/计算机设计大赛/cdc_face/keras-face-recognition-master/actualStudent.txt"; //定义学生文件的存放位置
        String absentStudentFilePath="E:/360Downloads/计算机设计大赛/cdc_face/keras-face-recognition-master/absentStudent.txt";
        try{
            String exe="python";
            String command="E:/360Downloads/计算机设计大赛/cdc_face/keras-face-recognition-master/face_recognize.py";
            String[] cmdArr = new String[] { exe, command };
            Process process = Runtime.getRuntime().exec(cmdArr);
        }catch (IOException e){
            e.printStackTrace();
        }
        BufferedReader reader=null;
        StringBuffer stringBuffer=new StringBuffer();
        try{
            reader=new BufferedReader(new FileReader(new File(actualStudentFilePath)));
            String tempStr;
            while((tempStr=reader.readLine())!=null){
                stringBuffer.append(tempStr);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String actualStudent=stringBuffer.toString();
        stringBuffer=new StringBuffer();
        try{
            reader=new BufferedReader(new FileReader(new File(absentStudentFilePath)));
            String tempStr;
            while((tempStr=reader.readLine())!=null){
                stringBuffer.append(tempStr);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String absentStudent=stringBuffer.toString();
        //新建签到对象
        Attendance attendance=new Attendance();
        //设置签到号
        List<Attendance> attendanceList=attendanceRepository.findAll();
        Integer newAttendanceNum=attendanceList.toArray().length+1;
        String number=String.valueOf(newAttendanceNum);
        String attendanceId="";
        for(int i=0;i<10-number.length();i++){
            attendanceId=attendanceId+"0";
        }
        attendanceId=attendanceId+number;
        attendance.setAttendanceNo(attendanceId);
        //设置周次几周
        String attendanceTime="";
        List<StartTime> startTimeList=timeRepository.findAll();
        String startTimeUp=startTimeList.get(0).getUp();//下学期
        String startTimeDown=startTimeList.get(0).getDown();//上学期
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //计算时间差
        int Days=TimeUtil.caculateTotalTime(startTimeDown,date.format(formatter));
        if(Days>0){
            int weeks=Days/7;
            attendanceTime=attendanceTime+String.valueOf(weeks+1)+"-"+String.valueOf(Days%7);
        }
        else{
            Days=TimeUtil.caculateTotalTime(startTimeUp,date.format(formatter));
            int weeks=Days/7;
            attendanceTime=attendanceTime+String.valueOf(weeks+1)+"-"+String.valueOf(Days%7);
        }
        attendance.setAttendanceTime(attendanceTime);

        //设置实到学生，缺席学生
        attendance.setAttendanceActualStudent(actualStudent);
        attendance.setAttendanceAbsentStudent(absentStudent);
        attendance.setAttendanceCourseNo(courseId);
        attendanceRepository.save(attendance);
    }
}
