package com.mytest.test;

import com.mytest.util.MyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerAdviceTest {

    /**
     * 拦截异常
     *
     * @return
     */
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Object myExceptionHandler(MyException e) {
        System.out.println("拦截异常 MyException: "
            + e.getMessage());
        return "拦截异常, 并处理";
    }
}
