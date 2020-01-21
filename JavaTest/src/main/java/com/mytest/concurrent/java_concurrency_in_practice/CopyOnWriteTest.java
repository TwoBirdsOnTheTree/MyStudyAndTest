package com.mytest.concurrent.java_concurrency_in_practice;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class CopyOnWriteTest {

    public static void main(String[] args) {
        CopyOnWriteTest t = new CopyOnWriteTest();

        t.test();
    }

    // @Test
    private void test() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        Iterator<String> iterator = list.iterator();

        new Thread(() -> {
            try {
                System.out.println("----------------------基于迭代器----------------------");
                // 基于迭代器, 此时迭代的是"快照snapshot版本的数组"
                // 即使其他线程修改(替换)了list内的数组, 这里的数组也不会变化
                for (String i : list) {
                    Thread.sleep(1000);
                    System.out.println("读线程, 当前是: " + i + ", 当前线程size是: " + list.size());
                    Thread.sleep(1000);
                }

                /*System.out.println("----------------------for循环----------------------");
                Thread.sleep(1000);
                // 不基于迭代器, 会实时获取最新的
                int now_list_size = list.size();
                // 这样的话, 其他线程remove会异常
                // for (int i = 0; i < now_list_size; i++) {
                // 实时的, 即使其他线程remove也不会异常, list.size()自动变小
                for (int i = 0; i < list.size(); i++) {
                    String ii = list.get(i);
                    System.out.println("读线程, 当前是: " + ii + ", 当前线程size是: " + list.size());
                    Thread.sleep(1000);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(500);
                list.remove(0);
                list.add(0, "d");
                System.out.println("CopyOnWrite put结束");
                Thread.sleep(1500);

                /*list.remove(2);
                System.out.println("CopyOnWrite remove结束");*/
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
