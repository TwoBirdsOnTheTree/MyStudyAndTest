package com.mytest.java8;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableTest {
    private static ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        new CompletableTest()
                //.simulate_js_promise()
                .simulate_js_promise_chain();
    }

    // 不能全局设置Executor?
    private void setCompletableFutureDefaultExecutor() {
        // CompletableFuture.screenExecutor();
    }

    private CompletableTest simulate_js_promise_chain() {
        CompletableFuture
                .supplyAsync(() -> {
                    sleep();
                    System.out.println("步骤 -> 1");
                    return "我是第一步的返回结果";
                }, executor)
                .thenApplyAsync((result) -> {
                    sleep();
                    System.out.println("第一步的执行结果是: " + result);
                    //TODO
                    // thenApplyAsync: 1.可以接收前一步的执行结果, 2.需要返回一个结果
                    return "第二部的执行结果";
                }, executor)
                .thenAcceptAsync((result) -> {
                    sleep();
                    System.out.println("第二步的执行结果是: " + result);
                    //TODO
                    // thenAcceptAsync: 1. 可以接收前一步的执行结果, 2. 相较于`thenApplyAsync`, 它不能返回结果
                    // return 0;
                }, executor)
                .thenRunAsync(() -> {
                    executor.shutdown();
                    //TODO
                    // thenRunAsync: 1. 不能接收前一步的执行结果, 2. Run: 不能有返回结果, Supply: 返回结果
                }, executor);

        return this;
    }

    // @Test
    CompletableTest simulate_js_promise() {
        CompletableFuture<Void> future = CompletableFuture
                .runAsync(() -> {
                    sleep();
                    System.out.println("步骤->1");
                }, executor)
                .thenRunAsync(() -> {
                    sleep();
                    System.out.println("步骤->2");
                }, executor)
                .thenRun(() -> {
                    executor.shutdown();
                    System.out.println("结束运行");
                });
        return this;
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
