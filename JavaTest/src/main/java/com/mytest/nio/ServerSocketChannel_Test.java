package com.mytest.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketChannel_Test {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel_Test t =
                new ServerSocketChannel_Test();

        t.test1();
    }

    void test1() throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap("Hello!".getBytes());

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress("localhost", 1234));
        // 默认是阻塞模式, 需要手动置为非阻塞模式
        System.out.println("isBlock: " + serverSocketChannel.isBlocking() + "\n");
        serverSocketChannel.configureBlocking(false);

        while (true) {
            System.out.print(".");

            // 非阻塞模式下, 这一步不会阻塞运行
            SocketChannel socketChannel = serverSocketChannel.accept();

            if (socketChannel == null) {
                Thread.sleep(200);
            } else {
                System.out.println("接收到请求");
                buffer.rewind();
                socketChannel.write(buffer);
                socketChannel.close();
            }
        }
    }
}
