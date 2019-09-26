package com.mytest.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropertiesTest implements ApplicationRunner {

    @Value("${application.title}")
    private String applicationTitle;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("获取Properties, application.title: "
            + applicationTitle);
    }
}
