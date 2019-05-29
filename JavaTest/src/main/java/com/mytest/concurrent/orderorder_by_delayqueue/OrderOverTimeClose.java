package com.mytest.concurrent.orderorder_by_delayqueue;

import java.util.Objects;
import java.util.concurrent.DelayQueue;

/**
 * [使用延时队列DelayQueue实现订单超时关闭]
 * [后台守护线程不断的执行检测工作]
 * [双检查模式实现单例模式]
 */
public class OrderOverTimeClose {

    private volatile static OrderOverTimeClose oderOverTimeClose = null;

    private OrderOverTimeClose() {

    }
    /**
     * 单例模式，双检查锁模式，在并发环境下对象只被初始化一次
     */
    public static OrderOverTimeClose getInstance(){
        if(oderOverTimeClose == null ){
            synchronized(OrderOverTimeClose.class){
                oderOverTimeClose =  new OrderOverTimeClose();
            }
        }
        return oderOverTimeClose;
    }

    /**
     * 守护线程
     */
    private Thread mainThread;

    /**
     * 启动方法
     */
    public void init(){
        mainThread =  new Thread(() -> execute());
        mainThread.setDaemon(true);
        mainThread.setName("守护线程-->");
        mainThread.start();
    }

    /**
     * 创建空延时队列
     */
    private DelayQueue<OrderInfo> queue = new DelayQueue<OrderInfo>();

    /**
     * 读取延时队列，关闭超时订单
     */
    private void execute() {
        while (true) {
            try {
                if(queue.size() > 0){
                    //从队列里获取超时的订单
                    OrderInfo orderInfo = queue.take();
                    // 检查订单状态，是否已经成功，成功则将订单从队列中删除。
                    if (Objects.equals(orderInfo.getStatus(), "成功")) {
                        System.out.println("线程："+Thread.currentThread().getName()+"，订单号："
                                + orderInfo.getOrderNo() + "，订单状态："
                                + orderInfo.getStatus() + "，订单创建时间："
                                + orderInfo.getCreateTime()
                                + "，订单超时时间：" + orderInfo.getExpTime()+"，当前时间："+OrderPay.getTime(System.currentTimeMillis()));
                        Thread.sleep(2000);
                    } else {
                        System.out.println("线程："+Thread.currentThread().getName()+"，订单号："
                                + orderInfo.getOrderNo() + "，变更订单状态为：超时关闭"
                                + "，订单创建时间："
                                + orderInfo.getCreateTime()
                                + "，订单超时时间：" + orderInfo.getExpTime()+"，当前时间："+OrderPay.getTime(System.currentTimeMillis()));
                        Thread.sleep(2000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入订单到超时队列中
     */
    public void orderPutQueue(OrderInfo orderInfo, String createTime,
                              String overTime) {
        System.out.println("订单号：" + orderInfo.getOrderNo() + "，订单创建时间："
                + createTime + "，订单过期时间：" + overTime);
        queue.add(orderInfo);
    }

}