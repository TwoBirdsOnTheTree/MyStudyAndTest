package com.test.dubbo.service;

import java.util.concurrent.CompletableFuture;

public interface AsyncService {
    // 返回结果为CompletableFuture类型, dubbo就自动将其异步执行了, 不需要设置其他东西
    CompletableFuture<String> asyncMethod();
}
