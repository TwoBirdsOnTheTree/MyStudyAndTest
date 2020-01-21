package com.mytest.concurrent;

import com.mytest.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadTest {
    public static void main(String[] args) throws Exception {
        ThreadTest t = new ThreadTest();

        // t.test();
        // t.thread_starvation_deadlock_test();
        // t.why_need_while_wait();
        t.notify_vs_notifyAll();
    }

    public void test() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                /*try {
                    Thread.sleep(1000);
                    System.out.println("线程循环执行");
                } catch (InterruptedException e) {
                    System.out.println("线程中断异常");
                    // e.printStackTrace();
                    // 恢复中断的状态
                    // Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }*/

                boolean interrupted = Thread.currentThread().isInterrupted();
                if (!interrupted)
                    System.out.println(".");
                else {
                    System.out.println("检测到本线程中断了");
                    // return;
                }
            }
        });

        thread.start();
        Thread.sleep(1500);
        System.out.println("-----------------mian线程执行中断-----------------");
        thread.interrupt();
    }

    // 线程饥饿死锁
    void thread_starvation_deadlock_test() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // 这样就不会线程饥饿死锁
        // ExecutorService executorService = Executors.newCachedThreadPool();

        FutureTask<String> a = new FutureTask<>(() -> {
            return "";
        });

        Thread b = new Thread(() -> {
            try {
                // 阻塞直到a结束, 但实际上a根本无法执行
                // 因为所有线程都被占用了 (被b占用了, b还在执行哦, 不会让出来给a执行的)
                String wait = a.get();
                System.out.println("wait: " + wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executorService.submit(b);
        executorService.submit(a);
        executorService.shutdown();
    }

    void why_need_while_wait() {
        List<String> list = new ArrayList<>();
        list.add("1");
        Object lock = new Object();

        new Thread(() -> {
            synchronized (lock) {
                if /*while*/ (list.size() == 1)
                    this.lock_wait(lock);
                list.add("2");
                System.out.println(list);
            }
        }).start();

        new Thread(() -> {
            synchronized (lock) {
                if /*while*/ (list.size() == 1)
                    this.lock_wait(lock);
                list.add("3");
                System.out.println(list);
            }
        }).start();

        Util.sleep(1000);
        synchronized (lock) {
            System.out.println("remove");
            list.remove("1");
            lock.notifyAll();
        }
    }

    void notify_vs_notifyAll() {
        Object lock = new Object();

        new Thread(() -> {
            synchronized (lock) {
                this.lock_wait(lock);
                System.out.println("1");
            }
        }).start();

        new Thread(() -> {
            synchronized (lock) {
                this.lock_wait(lock);
                System.out.println("2");
            }
        }).start();

        Util.sleep(1000);
        synchronized (lock) {
            lock.notify();
            // lock.notifyAll();
        }
    }

    // 隐藏try catch
    private void lock_wait(Object lock) {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
