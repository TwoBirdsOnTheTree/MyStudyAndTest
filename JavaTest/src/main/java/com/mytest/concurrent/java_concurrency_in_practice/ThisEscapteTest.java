package com.mytest.concurrent.java_concurrency_in_practice;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

public class ThisEscapteTest {
    interface Listener<T> {
        T doSomething();
    }


    public static void main(String[] args) throws InterruptedException {
        class Event {
            Object obj = null;

            <T> void addListener(Listener<T> t) {
                obj = t.doSomething();
            }
        }

        class ThisEscape {

            ThisEscape(Event event) {
                // 在构造方法里`发布`ThisEscape实例
                event.addListener(() -> this);

                // 构造方法延时结束
                try {
                    Thread.sleep(2000);
                    System.out.println("ThisEscape构造结束!!!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        Event event = new Event();

        new Thread(() -> {
            ThisEscape thisEscape = new ThisEscape(event);
        }).start();

        /**
         * 这个时候`this逸出`了
         * ThisEscape构造方法还没直接结束呢
         * 外部就可以访问ThisEscape实例了!
         */
        Thread.sleep(1000);
        // 访问ThisEscape实例
        ThisEscape escape = (ThisEscape) event.obj;
        System.out.println(escape.getClass().getName());
    }
}
