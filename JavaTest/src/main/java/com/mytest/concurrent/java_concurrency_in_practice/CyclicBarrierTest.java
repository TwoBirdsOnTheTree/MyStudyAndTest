package com.mytest.concurrent.java_concurrency_in_practice;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrierTest t = new CyclicBarrierTest();
        t.test();
    }

    public void test() {
        int c = 4;
        boolean execAwait = true;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(c + 1);

        IntStream.range(0, c).forEach(i -> {
            new Thread(() -> {
                try {
                    System.out.println("开始");
                    // 每个线程睡眠不同的时间
                    Thread.sleep(i * 1000);
                    // 调用await会阻塞, 并且导致CyblicBarrier的数字不断减一
                    // 但减到0的时候, 会解除阻塞状态
                    // 效果就是, 当没有await的时候, 打印"全部技术"是一个一个接着打印的, 但是有await的时候,
                    // 就是一起打印的
                    if (execAwait)
                        cyclicBarrier.await();
                    System.out.println("全部结束");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        // 手动控制, 实现类似CountDownLatch的功能
        try {
            Thread.sleep(10 * 1000);// 远大于其他线程的阻塞时间
            cyclicBarrier.await();// 解除其他线程的阻塞状态(同时也解除了自己的解除状态, 这点和CountDownLatch不同)
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
