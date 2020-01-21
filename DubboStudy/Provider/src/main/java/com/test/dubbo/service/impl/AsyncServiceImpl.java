package com.test.dubbo.service.impl;

import com.test.dubbo.service.AsyncService;

import java.util.concurrent.CompletableFuture;

public class AsyncServiceImpl implements AsyncService {
    private String str = "provider异步返回结果";

    @Override
    public CompletableFuture<String> asyncMethod() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return str;
        });
    }
}
