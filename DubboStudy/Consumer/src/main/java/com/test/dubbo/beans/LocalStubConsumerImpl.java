package com.test.dubbo.beans;

import com.test.dubbo.service.LocalStubService;

public class LocalStubConsumerImpl implements LocalStubService {
    private LocalStubService localStubService;

    public LocalStubConsumerImpl(LocalStubService localStubService) {
        this.localStubService = localStubService;
    }

    @Override
    public String sayHello(String str) {
        // 先执行stub (LocalStubConsumerImpl的sayHello),
        System.out.println("8. 先执行Stub的方法");
        // 再手动调用dobbo(可选)
        String s = localStubService.sayHello(str);
        s = "调用Stub的返回结果是: " + s;
        return s;
    }
}
