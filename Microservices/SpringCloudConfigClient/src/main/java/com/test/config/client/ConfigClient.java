package com.test.config.client;

import com.test.config.client.bean.TracerForProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
public class ConfigClient {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClient.class, args);
    }

    @Value("${tracer.property}")
    private String configProperty;
    @Autowired
    private TracerForProperties tracerForProperties;

    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        return "测试获取SpringCloudConfigServer的配置：" + configProperty
                + ",tracer bean: " + tracerForProperties.getProperty();
    }
}
