package com.mytest.concurrent.java_concurrency_in_practice;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ExecutorsTest {
    public static void main(String[] args) throws Exception {
        ExecutorsTest t = new ExecutorsTest();
        // t.test1();
        // t.test2();
        t.test3();
    }

    // ScheculedExecutorService 定时执行
    void test1() {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(5);

        System.out.println("1");

        scheduledExecutorService.schedule(() -> {
            System.out.println("延时执行");
        }, 10, TimeUnit.SECONDS);

        System.out.println("2");

        final int[] i = new int[]{0};
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            i[0] = i[0] + 1;
            System.out.println("  第" + i[0] + "秒");

            // 定时13秒关闭
            if (i[0] >= 13) {
                scheduledExecutorService.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);

        // 周期任务不能shutdown, 否则会阻止下一次周期的任务执行
        // scheduledExecutorService.shutdown();
    }

    // 饱和策略
    void test2() throws InterruptedException {
        RejectedExecutionHandler abort = new ThreadPoolExecutor.AbortPolicy();
        RejectedExecutionHandler discard = new ThreadPoolExecutor.DiscardPolicy();
        RejectedExecutionHandler discardOldest = new ThreadPoolExecutor.DiscardOldestPolicy();
        RejectedExecutionHandler CallerRuns = new ThreadPoolExecutor.CallerRunsPolicy();


        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(0, 1,
                        60L, TimeUnit.MILLISECONDS,
                        new ArrayBlockingQueue<>(1),
                        // abort
                        // discard
                        // discardOldest
                        CallerRuns
                );

        // 占满执行线程
        threadPoolExecutor.execute(() -> {
            try {
                System.out.println("第一个线程, 线程是: " + Thread.currentThread().getName());
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(500);

        // 占满有界队列
        threadPoolExecutor.execute(() -> {
            System.out.println("第二个线程, 线程是: " + Thread.currentThread().getName());
        });

        // 尝试继续添加, 开始饱和策略
        try {
            threadPoolExecutor.execute(() -> {
                try {
                    // 如果是CallerRuns, 线程名是: main (调用者是main线程)
                    System.out.println("第三个线程, 线程是: " + Thread.currentThread().getName());
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 如果是CallerRuns, 这句话需要在当前线程任务执行完后才执行
        System.out.println("shutdown");
        threadPoolExecutor.shutdown();
    }

    // 扩展ThreadPoolExecutor
    void test3() {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(0, 10,
                        60L, TimeUnit.MILLISECONDS,
                        new ArrayBlockingQueue<>(10)) {
                    @Override
                    protected void beforeExecute(Thread t, Runnable r) {
                        System.out.println("beforeExecute");
                        super.beforeExecute(t, r);
                    }

                    @Override
                    protected void afterExecute(Runnable r, Throwable t) {
                        System.out.println("afterExecute");
                        super.afterExecute(r, t);
                    }

                    @Override
                    protected void terminated() {
                        System.out.println("terminated");
                        super.terminated();
                    }
                };

        threadPoolExecutor.execute(() -> {
            System.out.println("第一任务");
        });

        threadPoolExecutor.execute(() -> {
            System.out.println("第二任务");
        });

        threadPoolExecutor.execute(() -> {
            System.out.println("第三任务");
        });

        threadPoolExecutor.shutdown();
    }

    void sdfsdf() {
        CompletableFuture.runAsync(() -> {

        });

        IntStream.range(0, 10)
                .parallel()
                .boxed();
    }
}
