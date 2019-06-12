package com.mytest.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DatagramChannel_connect_read_write_test {
    class Receive {
        DatagramChannel receiveChannel;
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

        void init() throws Exception {
            this.receiveChannel = DatagramChannel.open();
            this.receiveChannel.configureBlocking(false);
            this.receiveChannel.bind(new InetSocketAddress("localhost", 1234));
            this.receiveChannel.connect(new InetSocketAddress("localhost", 1234));

            new Thread(() -> {
                try {
                    while (true) {
                        if (this.receiveChannel.isConnected())
                            break;
                        System.out.println('.');
                        Thread.sleep(200);
                    }
                    System.out.println("服务端已建立连接");
                    while (true) {
                        this.receiveChannel.read(buffer);
                        if (buffer.position() != 0) {
                            buffer.flip();
                            System.out.println("接收到请求: "
                                    + new String(buffer.array(), 0, buffer.remaining()));
                            buffer.clear();
                        } else {
                            System.out.print('-');
                            Thread.sleep(200);
                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    class Send {
        DatagramChannel receiveChannel;

        void init() throws Exception {
            DatagramChannel sendChannel = DatagramChannel.open();
            sendChannel.configureBlocking(false);
            sendChannel.connect(new InetSocketAddress("localhost", 1234));
            System.out.println("与服务端建立连接了吗: " + sendChannel.isConnected());

            Thread.sleep(1500);

            System.out.println("发送数据了->");
            sendChannel.write(ByteBuffer.wrap("你好啊".getBytes()));
        }
    }


    public static void main(String[] args) throws Exception {
        DatagramChannel_connect_read_write_test t = new DatagramChannel_connect_read_write_test();
        t.new Receive().init();

        Thread.sleep(500);
        t.new Send().init();
    }
}
