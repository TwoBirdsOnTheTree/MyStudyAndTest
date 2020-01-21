package com.mytest.concurrent.java_concurrency_in_practice;

import java.util.concurrent.Exchanger;

public class ExchangerTest {
    public static void main(String args[]) {
        ExchangerTest t = new ExchangerTest();
        t.test1();
    }

    void test1() {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                String set = "set初始值";
                System.out.println("开始exchanger");
                // 阻塞
                set = exchanger.exchange(set);
                System.out.println("结束exchanger, set: " + set);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                String get = "get初始值";
                System.out.println("  开始exchanger");
                get = exchanger.exchange(get);
                System.out.println("  接收exchanger, get: " + get);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
