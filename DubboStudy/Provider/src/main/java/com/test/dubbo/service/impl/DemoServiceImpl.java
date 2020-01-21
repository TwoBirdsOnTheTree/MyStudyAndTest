package com.test.dubbo.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.test.dubbo.service.DemoService;

import java.util.Map;

public class DemoServiceImpl implements DemoService {
    public String sayHello() {
        System.out.println("Hello! Dubbo Provider");

        Map<String, String> attachments = RpcContext.getContext().getAttachments();
        if (null != attachments.get("atta")) {
            System.out.println("provider获取到attachment: " + attachments.get("atta"));
        }

        return "string from dubbo provider";
    }
}
