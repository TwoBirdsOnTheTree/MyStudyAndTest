package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
// 启动Eureka服务发现server端
@EnableEurekaServer
@RestController
public class EurekaServer {
    public static void main(String[] args) {
        new SpringApplication(EurekaServer.class).run(args);
    }

    @RequestMapping("hi")
    public String hi() {
        return "eureka server!";
    }
}
