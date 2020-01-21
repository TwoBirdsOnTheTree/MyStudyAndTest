package com.mytest.concurrent.java_concurrency_in_practice;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLockTest t = new ReentrantLockTest();
        // t.test1();
        // t.test2();
        // t.test3();
        t.test4();
    }

    // lock
    void test1() {
        Lock lock = new ReentrantLock();
        lock.lock();

        new Thread(() -> {
            System.out.println("尝试获取锁");
            lock.lock();
            try {
                System.out.println("获取到锁");
            } finally {
                lock.unlock();
                System.out.println("释放掉锁");
            }
        }).start();
    }

    // tryLock
    void test2() {
        Lock lock = new ReentrantLock();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("线程1获取到锁");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);

                System.out.println("线程2尝试获取到锁");
                boolean ifLock = lock.tryLock();
                System.out.println("是否获取到锁: " + ifLock);

                // 定时锁(会阻塞)
                boolean ifBlock2 = lock.tryLock(1 /*3*/, TimeUnit.SECONDS);
                System.out.println("是否获取到锁: " + ifBlock2);

                if (ifBlock2) {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void test3() throws InterruptedException {
        Lock lock = new ReentrantLock();
        lock.lock();

        Thread thread = new Thread(() -> {
            try {
                System.out.println("获取可中断的锁");
                lock.lockInterruptibly();
//                 System.out.println("获取不可中断的锁");
//                 lock.lock();
                System.out.println("获取到锁");
                lock.unlock();
            } catch (Exception e) {
                System.out.println("响应中断异常");
                e.printStackTrace();
            }
        });
        thread.start();

        Thread.sleep(500);
        System.out.println("线程触发中断");
        thread.interrupt();
    }

    void test4() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();

        new Thread(() -> {
            lock.unlock();// 能否释放掉其他线程的锁? 肯定不能
            lock.lock();
            System.out.println("获取到锁");
        }).start();
    }
}
