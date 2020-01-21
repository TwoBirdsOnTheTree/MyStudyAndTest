package com.mytest.concurrent.java_concurrency_in_practice;

import java.util.stream.IntStream;

/**
 *
 */
public class NoVisibility {
    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.print(0 == number ? "-------------: 0! \n" : ".");
        }

        public static void main(String[] args) throws InterruptedException {
            IntStream.range(0, 100).forEach(i -> {
                try {
                    new ReaderThread().start();

                    number = 42;
                    ready = true;

                    Thread.sleep(100);
                    System.out.println("执行第: " + i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
