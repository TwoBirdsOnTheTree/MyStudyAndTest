package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
// 启动Eureka客户端
@EnableEurekaClient

@RestController
public class EurekaServiceB {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceB.class, args);
    }

   private Logger logger = LoggerFactory.getLogger(EurekaServiceB.class);

    // 测试被其他服务调用
    @RequestMapping("hi")
    public String test(HttpServletRequest request) {
        logger.info("hi-");
        String testParam = request.getParameter("testParam");
        String body = "";
        try (ServletInputStream inputStream = request.getInputStream()) {
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(bytes)) != -1) {
                body += new String(bytes, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "return from eureka-service-b, testParam = " + testParam + ", body = " + body;
    }

    @RequestMapping("/test_timeout")
    public String test_timeout() {
        System.out.println("eureka-service-b收到请求，测试熔断");
        sleep(30);
        return "success";
    }

    private void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
