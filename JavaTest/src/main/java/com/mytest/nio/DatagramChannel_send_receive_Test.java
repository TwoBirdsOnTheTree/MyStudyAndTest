package com.mytest.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.TimeUnit;

public class DatagramChannel_send_receive_Test {
    public static void main(String[] args) throws Exception {
        new SendReceiveTest().send();
    }
}


class SendReceiveTest {

    class Receive {
        DatagramChannel receiveChannel;
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

        void init() throws Exception {
            this.receiveChannel = DatagramChannel.open();
            this.receiveChannel.configureBlocking(false);
            this.receiveChannel.bind(new InetSocketAddress("localhost", 1234));

            new Thread(() -> {
                while (true) {
                    try {
                        // 非阻塞模式下, receive方法不会被阻塞
                        if (this.receiveChannel.receive(buffer) != null) {
                            System.out.println("接收到请求");
                            buffer.flip();
                            System.out.println(new String(buffer.array(), 0, buffer.remaining()));
                            buffer.clear();

                        } else {
                            System.out.print('-');
                            Thread.sleep(200);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }

    void send() throws Exception {
        new Receive().init();

        Thread.sleep(1500);

        System.out.println("\n发送请求->");
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        datagramChannel.send(ByteBuffer.wrap("你好啊".getBytes()),
                new InetSocketAddress("localhost", 1234));

        Thread.sleep(1500);

        System.out.println("\n第二次发送请求->");
        datagramChannel.send(ByteBuffer.wrap("你好啊22222".getBytes()),
                new InetSocketAddress("localhost", 1234));
    }
}