package com.mytest.concurrent.java_concurrency_in_practice;

import com.mytest.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
    public static void main(String[] args) {
        ReadWriteLockTest t = new ReadWriteLockTest();
        // t.test1();
        t.test2();
    }

    void test1() {
        MyMap map = new MyMap();

        new Thread(() -> {
            // write
            System.out.println("写入开始");
            // map.put("write", "1", null);
            map.put("write", "1", 5000);// 写锁
            System.out.println("写入结束");
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(500);

                // 阻塞直到写锁释放
                // read
                System.out.println("开始读");
                map.get("read", null);
                System.out.println("结束读");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 写锁是排他的
    void test2() {
        MyMap map = new MyMap();

        new Thread(() -> {
            Util.sleep(500);
            // map.get("write", 5);
            // System.out.println("读锁结束");
            map.put("write", "1", 5000);
            System.out.println("写锁结束");
        }).start();
        new Thread(() -> {
            // Util.sleep(500);
            map.get("write", 5000);
            System.out.println("读锁结束");
        }).start();
    }

    // 可重入读写锁
    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();

    // 通过读写锁改写Map
    class MyMap {
        Map<String, String> map = new HashMap<>();

        void put(String key, String value, Integer sleepMilliSeconds) {
            writeLock.lock();
            try {
                // 写锁中可以读？是可以降级的
                // String getRead = this.get("write", null);// 申请读锁
                // System.out.println("写锁中可以读? read: " + getRead);
                map.put(key, value);
                if (Objects.nonNull(sleepMilliSeconds)) {
                    Util.sleep(sleepMilliSeconds);
                }
            } finally {
                writeLock.unlock();
            }
        }

        String get(String key, Integer sleepMilliSeconds) {
            readLock.lock();
            try {
                if (Objects.nonNull(sleepMilliSeconds)) {
                    Util.sleep(sleepMilliSeconds);
                }
                return map.get(key);
            } finally {
                readLock.unlock();
            }
        }
    }
}
