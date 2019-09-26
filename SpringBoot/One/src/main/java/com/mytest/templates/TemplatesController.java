package com.mytest.templates;

import com.mytest.util.MyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TemplatesController {

    @RequestMapping("/go-test")
    public String test() {
        return "/test";
    }

    @RequestMapping("/my-exception")
    public void exception() {
        throw new MyException("自定义抛出的异常");
    }

    @RequestMapping("/exception")
    public void exception2() {
        throw new RuntimeException("异常");
    }
}
