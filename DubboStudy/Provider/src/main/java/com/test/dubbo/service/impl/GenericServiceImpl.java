package com.test.dubbo.service.impl;

import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;

public class GenericServiceImpl implements GenericService {
    @Override
    public Object $invoke(String s, String[] strings, Object[] objects) throws GenericException {
        System.out.println(String.format("GenericService方法名: %s, 参数: %s", s, objects));
        return "GenericService定义的返回值";
    }
}
