package com.test.dubbo.service.impl;

import com.test.dubbo.service.DemoService1;

public class DemoService1Impl implements DemoService1 {
    @Override
    public String testMethod(String str) {
        System.out.println("参数: " + str);
        return "dubbo泛化实例返回结果";
    }
}
