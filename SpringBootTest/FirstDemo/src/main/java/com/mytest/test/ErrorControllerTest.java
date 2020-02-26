package com.mytest.test;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理
 * 通过实现ErrorController并映射/error路径
 * (/error是Spring Boot自动提供的, 专门用于errors处理)
 */
// 先注释掉不要让其生效
// @Controller
public class ErrorControllerTest implements ErrorController {

    // @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping("/error")
    // @ResponseBody
    public String error(HttpServletResponse response)
        throws Exception {
        String path = getErrorPath();

        response.getOutputStream().print(path);

        return path;
    }
}
