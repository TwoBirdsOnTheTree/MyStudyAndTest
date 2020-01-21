package com.mytest.concurrent.java_concurrency_in_practice;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchTest t = new CountDownLatchTest();
        t.test1();
    }

    // CountDownLatch 可以阻塞多个线程, 但countdown结束时会同时解除所有阻塞状态
    public void test1() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            try {
                System.out.println("线程1开始");
                // 可以阻止多个线程
                latch.await();
                System.out.println("线程1执行结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("线程2开始");
                // 可以阻止多个线程
                latch.await();
                System.out.println("线程2执行结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("------------main线程开始, 执行阻塞------------\n");
        Thread.sleep(1000);
        System.out.println("------------latch.countDown了------------\n");
        latch.countDown(); // 解除阻塞
    }

}
