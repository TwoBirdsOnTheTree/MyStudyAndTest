package com.test.dubbo.service.impl;

import com.test.dubbo.service.LocalStubService;

public class LocalStubServiceImpl implements LocalStubService {
    @Override
    public String sayHello(String str) {
        System.out.println("执行LocalStubServiceImpl.sayHello");
        return "Provider 的 LocalStubServiceImpl 返回结果, 参数是 " + str;
    }
}
