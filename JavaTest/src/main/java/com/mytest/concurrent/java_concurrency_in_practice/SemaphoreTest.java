package com.mytest.concurrent.java_concurrency_in_practice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class SemaphoreTest {
    public static void main(String[] args) {
        SemaphoreTest t = new SemaphoreTest();
        // t.test();
        // t.test2();
        t.test3();
    }

    public void test3() {
        Semaphore semaphore = new Semaphore(5);
        IntStream.range(0, 5).forEach(i -> {
            try {
                semaphore.acquire();
                System.out.println("占用许可: " + i);
            } catch (InterruptedException e) {
                System.out.println("Semaphore.acquire抛出InterruptedException");
                e.printStackTrace();
            }
        });
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            System.out.println("ssss");
            e.printStackTrace();
        }
    }

    public void test() {
        // 仅3个位子
        Semaphore semaphore = new Semaphore(3);
        // Semaphore semaphore = new Semaphore(1);

        // 20个人上厕所
        IntStream.range(0, 20).forEach(i -> {
            new Thread(() -> {
                try {
                    System.out.println("排队上厕所..., 等待");
                    // 所有线程在此阻塞
                    // 3个(信号量)一起执行
                    semaphore.acquire();

                    Thread.sleep(1000);
                    // 可以发现是3个/3个一起打印的, 表示是3个一起执行
                    System.out.println("结束上厕所");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    // 模拟有界list(线程不安全)
    public void test2() {
        class BoundedList<T> {
            private List<T> list = new ArrayList<>();
            // 有界队列, 可以存储5个数据
            private Semaphore semaphore = new Semaphore(5);

            public void add(T t) throws InterruptedException {
                int oldSize = list.size();
                semaphore.acquire();
                list.add(t);
                int nowSize = list.size();
                System.out.println("list add前size: " + oldSize + ", add后size: " + nowSize);
            }

            public void removeFirst() {
                list.remove(0);
                semaphore.release();
            }
        }

        BoundedList<Integer> list = new BoundedList<>();

        new Thread(() -> {
            IntStream.range(0, 10).forEach(i -> {
                // 每1s删除第一个
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Semaphore.release执行, 删除第一个!");
                list.removeFirst();
            });
        }).start();

        // 模拟填充数据, 10个
        IntStream.range(0, 10).forEach(i -> {
            try {
                list.add(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
