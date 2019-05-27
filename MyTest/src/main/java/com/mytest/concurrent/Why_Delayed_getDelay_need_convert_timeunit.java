package com.mytest.concurrent;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 为何 Delayed的getDelayed方法, 需要转换时间单位
 */
public class Why_Delayed_getDelay_need_convert_timeunit {
    static AtomicInteger getDelayCount = new AtomicInteger(0);

    public static void main(String[] args) {
        new Why_Delayed_getDelay_need_convert_timeunit()
                .test();
    }

    void test() {
        // 定义一个 10s 超时的
        Order order = new Order();
        order.expireTime = new Date(System.currentTimeMillis() + 10000);

        // 加入 delayqueue
        DelayQueue<Order> queue = new DelayQueue<>();
        queue.add(order);

        // 线程获取
        new Thread(() -> {
            while (true) {
                try {
                    Order take = queue.take();
                    System.out.println("取到order了, getDelayCount: " + getDelayCount.get());
                    //TODO
                    // millisecond getDelay次数是: 217960, cpu会有一次小波峰
                    // nanasecond getDelay只有2次!!!! cpu无变化
                    //
                    //


                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class Order implements Delayed {
        public Date expireTime;

        @Override
        public long getDelay(TimeUnit unit) {
            // 超时时间, 但是是: 毫秒
            long expireMilliSeconds = expireTime.getTime() - System.currentTimeMillis();
            // 按照方法参数的时间单位, 进行转换
            long convertToParamUnit = unit.convert(expireMilliSeconds, TimeUnit.MILLISECONDS);
            //TODO 切换时间单位
            // long result = expireMilliSeconds;
            long result = convertToParamUnit;
            System.out.println("getDelay: " + result);
            getDelayCount.getAndAdd(1);
            return result;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }
}
