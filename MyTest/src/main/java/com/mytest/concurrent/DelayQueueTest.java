package com.mytest.concurrent;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class DelayQueueTest {

    public static void main(String[] args) {
        new DelayQueueTest().my_test_auto_close_order_by_delayqueue();
    }

    //TODO 用@test导致程序自动关闭了...shit
    // @Test
    void my_test_auto_close_order_by_delayqueue() {
        // 订单
        class Order implements Delayed {
            public String orderNo;
            public Date createTime;
            public Date expireTime;

            @Override
            public long getDelay(TimeUnit unit) {
                // System.out.println("是否一直在调用getDelay, 判断是否为0?"); // 是的, 一直再调用
                // return expireTime.getTime() - System.currentTimeMillis();
                long expireMilliSeconds = expireTime.getTime() - System.currentTimeMillis();
                return unit.convert(expireMilliSeconds, TimeUnit.MILLISECONDS);
            }

            @Override
            public int compareTo(Delayed o) {
                //TODO 强转?
                Order other = (Order) o;
                return this.expireTime.compareTo(other.expireTime);
            }
        }

        // 订单关闭
        class OrderClose {
            private DelayQueue<Order> queue = new DelayQueue<>();
            private Thread thread = new Thread(() -> {
                while (true) {
                    // System.out.println("测试时候会阻塞");
                    try {
                        Order order = queue.take();
                        String expireTime = new SimpleDateFormat("HH:mm:ss").format(order.expireTime);
                        System.out.println("       DelayQueue订单: " + expireTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        OrderClose orderClose = new OrderClose();
        orderClose.thread.start();

        // 生成订单
        Supplier<Order> generateOrder = () -> {
            Order order = new Order();
            order.orderNo = UUID.randomUUID().toString();
            order.createTime = new Date();
            order.expireTime = new Date(System.currentTimeMillis() + 10000);
            return order;
        };

        IntStream.range(0, 20).forEach(i -> {
            new Thread(() -> {
                while (true) {
                    try {
                        Order order = generateOrder.get();
                        String expireTime = new SimpleDateFormat("HH:mm:ss").format(order.expireTime);
                        System.out.println("生成订单, expireTime: " + expireTime);
                        // 加入延迟队列
                        orderClose.queue.add(order);
                        // 效果完全一样: orderClose.queue.offer(order);
                        // 模拟随机生成订单
                        int randomDelay = (int) (Math.random() * 5);
                        Thread.sleep(randomDelay * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });
    }

    @Test
    void test_random() {
        IntStream.range(0, 100).forEach(i -> {
            System.out.println((int) (Math.random() * 5));
            System.out.println(UUID.randomUUID().toString());
        });
    }
}
