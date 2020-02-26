package com.test;

import com.alibaba.fastjson.JSON;

public class MainClass {
    public static void main(String[] args) {
        // 测试jar包启动的mainClass
        System.out.println("指定启动的MainClass成功！");
        // 测试打包时依赖大包
        System.out.println(JSON.toJSONString("测试打包是否包含依赖的jar包"));
    }
}
