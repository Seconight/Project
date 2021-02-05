package com.Attendance.student_sign_demo.service.impl;

import com.Attendance.student_sign_demo.entity.*;
import com.Attendance.student_sign_demo.form.AttendanceForm;
import com.Attendance.student_sign_demo.form.CourseForm;
import com.Attendance.student_sign_demo.repository.*;
import com.Attendance.student_sign_demo.service.TeacherService;
import com.Attendance.student_sign_demo.util.PathUtil;
import com.Attendance.student_sign_demo.util.TimeUtil;
import com.Attendance.student_sign_demo.vo.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.ui.context.Theme;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Future;
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
    @Async("asyncServiceExecutor")
    public Future<List<TeacherCourseVO>> getCourses(String id)throws Exception {
        String[] courseNo=teacherRepository.findByTeacherNo(id).getTeacherCourse();
        List<TeacherCourseVO> teacherCourseVOList=new ArrayList<>();
        List<Course> courseList=new ArrayList<>();
        List<String> semesterList=new ArrayList<>();//新建学期列表
        for(int i=0;i<courseNo.length;i++)
        {
            Course course=courseRepository.findByCourseNo(courseNo[i]);
            if(semesterList.toArray().length==0){//列表为空直接添加
                semesterList.add(course.getCourseSemester());
            }
            else{
                if(!semesterList.contains(course.getCourseSemester())){
                    semesterList.add(course.getCourseSemester());//列表不为空比较后添加
                }
            }
            courseList.add(course);
        }
        for(int i=0;i<semesterList.toArray().length;i++){//遍历学期列表创建courseVO对象
            TeacherCourseVO teacherCourseVO=new TeacherCourseVO();
            teacherCourseVO.setSemester(semesterList.get(i));
            List<TeacherCourse1VO> course1VOList=new ArrayList<>();
            for(int j=0;j<courseList.toArray().length;j++){//取出合适学期的课程
                if(courseList.get(j).getCourseSemester().equals(semesterList.get(i))){
                    Course course=courseList.get(j);
                    TeacherCourse1VO course1VO=new TeacherCourse1VO();
                    String[] students=course.getCourseShouldStudent();
                    course1VO.setId(course.getCourseNo());
                    course1VO.setDays(course.getCourseDays());
                    course1VO.setEndTime(course.getCourseEndTime());
                    course1VO.setName(course.getCourseName());
                    course1VO.setWeeks(course.getCourseWeeks());
                    course1VO.setStartTime(course.getCourseStartTime());
                    course1VO.setStudentNum(students.length);
                    course1VOList.add(course1VO);
                }
            }
            teacherCourseVO.setTeacherCourse1VOList(course1VOList);
            teacherCourseVOList.add(teacherCourseVO);
        }
        return new AsyncResult<>(teacherCourseVOList);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<List<CourseStudentVO>> getCourseStudent(String courseId)throws Exception {
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
        return new AsyncResult<>(courseStudentVOList);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<List<AttendanceVO>> getAttendanceInfo(String courseId)throws Exception {
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
        return new AsyncResult<>(attendanceVOList);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> supplyAttendance(String studentId,String attendanceId) throws Exception{
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
        return new AsyncResult<>(true);
    }

    //老师新建课程
    @Override
    @Async("asyncServiceExecutor")
    public void newCourse(CourseForm courseForm) {

        String courseName = courseForm.getName();
        Integer startTime = courseForm.getsTime();
        Integer endTime = courseForm.geteTime();
        String days = courseForm.getDay();
        String weeks = courseForm.getWeek();
        String semester = courseForm.getSemester();
        String teacherId = courseForm.getTeacherId();
        MultipartFile studentsFile = courseForm.getStudents();
        if(courseForm.getStudents()==null){
            System.out.println("the file is null！");
        }
        String filePath=PathUtil.demoPath+"/students/"+courseName+".xlsx";
        try {
            studentsFile.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
           //XSSFWorkbook workbook = new XSSFWorkbook(studentsFile.getInputStream());
           XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
           //HSSFWorkbook workbook=new HSSFWorkbook(studentsFile.getInputStream());
           XSSFSheet hssfSheet=workbook.getSheetAt(0);//获取表单
           int numOfRows=hssfSheet.getPhysicalNumberOfRows();//获取行数
           for(int i=1;i<numOfRows;i++)
           {
               XSSFRow hssfRow=hssfSheet.getRow(i);//获取行
               if(hssfRow==null){
                   continue;//空行跳过
               }
               XSSFCell hssfCell=hssfRow.getCell(0);//第二行一列数据
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

    //签到函数
    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> Sign(AttendanceForm attendanceForm)throws Exception {
        MultipartFile imageFile = attendanceForm.getImg();
        String courseId = attendanceForm.getId();
        //先将文件存到本地
        String filePath=PathUtil.demoPath+"/attendance.jpg"; //定义上传文件的存放位置
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
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(new File(PathUtil.demoPath+"/shouldStudents.txt")));
            bufferedWriter.write(studentAndEncoding);
            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        //进行人脸识别将识别出的人脸保存到文件里
        String actualStudentFilePath=PathUtil.demoPath+"/actualStudent.txt"; //定义学生文件的存放位置
        String absentStudentFilePath=PathUtil.demoPath+"/absentStudent.txt";
        try{
//            String exe="python";
//            String command="E:/360Downloads/计算机设计大赛/cdc_face/keras-face-recognition-master/face_recognize.py";
//            String[] cmdArr = new String[] { exe, command };
//            Process process = Runtime.getRuntime().exec(cmdArr);
            Process process = Runtime.getRuntime().exec(
                    "cmd.exe /c start "+ PathUtil.demoPath+"/runRecognize.bat ");
            process.waitFor();
//            InputStream in = process.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            String tmp = null;
//            while ((tmp = br.readLine()) != null) {
//                // do nothing
//            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //File actualStudentFile = new File(actualStudentFilePath)



        BufferedReader reader=null;
        StringBuffer stringBuffer=new StringBuffer();
        try{
            reader=new BufferedReader(new FileReader(new File(actualStudentFilePath)));
            String tempStr;
            while((tempStr=reader.readLine())!=null){
                stringBuffer.append(tempStr);
            }
            reader.close();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
        return new AsyncResult<>(true);
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<Boolean> Sign(String courseId, MultipartFile[] imageList)throws Exception{
        //根据课程号把学生名单以及对应的人脸encoding保存到文件中
        String [] studentsId=courseRepository.findByCourseNo(courseId).getCourseShouldStudent();
        String studentAndEncoding="";
        for(int i=0;i<studentsId.length;i++){
            Student student=studentRepository.findByStudentNo(studentsId[i]);
            String[] encodings=student.getStudentEncoding().split(";");
            for(int j=0;j<encodings.length;j++)
                studentAndEncoding=studentAndEncoding+studentsId[i]+":"+encodings[j]+";";
        }
        studentAndEncoding=studentAndEncoding.substring(0,studentAndEncoding.length()-1);
        try{
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(new File(PathUtil.demoPath+"/shouldStudents.txt")));
            bufferedWriter.write(studentAndEncoding);
            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        String actualStudentFilePath=PathUtil.demoPath+"/actualStudent.txt"; //定义学生文件的存放位置
        String absentStudentFilePath=PathUtil.demoPath+"/absentStudent.txt";
        String filePath=PathUtil.demoPath+"/attendance.jpg"; //定义上传文件的存放位置
        String actualStudent="";
        String absentStudent="";
        for(int i=0;i<imageList.length;i++){
            //将传来的图片保存到本地
            try{
                imageList[i].transferTo(new File(filePath));
            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            //进行人脸识别将识别出的人脸保存到文件里
            try{
                Process process = Runtime.getRuntime().exec(
                        "cmd.exe /c start "+ PathUtil.demoPath+"/runRecognize.bat ");
                process.waitFor();
            }catch (Exception e){
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
            actualStudent=actualStudent+stringBuffer.toString();
            //缺席学生没必要
//            stringBuffer=new StringBuffer();
//            try{
//                reader=new BufferedReader(new FileReader(new File(absentStudentFilePath)));
//                String tempStr;
//                while((tempStr=reader.readLine())!=null){
//                    stringBuffer.append(tempStr);
//                }
//                reader.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            absentStudent=absentStudent+stringBuffer.toString();
        }
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
        String [] actualStudentString=actualStudent.substring(0,actualStudent.length()-1).split(",");
        Set set = new HashSet();
        for (int i = 0; i < actualStudentString.length; i++) {
            set.add(actualStudentString[i]);
        }
        actualStudentString = (String[]) set.toArray(new String[0]);
        String totalActualStudent="";
        for(int i=0;i<actualStudentString.length;i++){
            totalActualStudent=totalActualStudent+actualStudentString[i]+",";
        }
        //求缺席学生
        for(int i=0;i<studentsId.length;i++){
            if(!set.contains(studentsId[i])){
                absentStudent=absentStudent+studentsId[i]+",";
            }
        }
        attendance.setAttendanceActualStudent(totalActualStudent.substring(0,totalActualStudent.length()-1));
        attendance.setAttendanceAbsentStudent(absentStudent.substring(0,absentStudent.length()-1));
        attendance.setAttendanceCourseNo(courseId);
        attendanceRepository.save(attendance);
        return new AsyncResult<>(true);
    }
}
