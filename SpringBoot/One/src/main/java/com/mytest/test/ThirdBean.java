package com.mytest.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThirdBean {
    @Autowired
    public ConfigurationBeanTest.BeanTest bean;
}
