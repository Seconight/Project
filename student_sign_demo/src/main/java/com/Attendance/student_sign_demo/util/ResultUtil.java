package com.Attendance.student_sign_demo.util;

import com.Attendance.student_sign_demo.vo.ResultVO;


//封装返回值
public class ResultUtil {
    //success
    public static ResultVO success(Object data){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(1);
        resultVO.setMessage("成功");
        resultVO.setData(data);
        return resultVO;
    }

    public static ResultVO failed(String message){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMessage("失败");
        resultVO.setData(message);
        return resultVO;
    }
}

