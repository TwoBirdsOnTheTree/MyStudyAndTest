package com.mytest.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 对java 网络编程不懂啊.. 需要学习...
 * 简单点来说:
 * socket / socketchannel 用于客户端
 * serversocket / serversocketchannel 作为服务端 (但是也用到了socketchannel, 是作为连接用的吧)
 * <p>
 * 所以没有启动服务端时, 这里就会运行错误, 提示connection refused
 */
public class SocketChannelTest {
    public static void main(String[] args) throws Exception {
        SocketChannelTest t = new SocketChannelTest();
        // t.test1();
        t.can_socket_bind_used_as_server();
        // Example_from_Java_Nio_book.main(null);
    }

    //TODO
    // 那Socket的bind()干啥的?
    // 并不能做服务端啊....
    void can_socket_bind_used_as_server() throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        SocketChannel bindSocketChannel = socketChannel.bind(new InetSocketAddress("localhost", 1234));

        while (true) {
            if (bindSocketChannel.isConnected()) {
                System.out.println("连接了?");
                break;
            } else {
                System.out.print('.');
                Thread.sleep(200);
            }
        }

    }

    void test1() throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap("你好啊".getBytes());

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 1234));

        while (true) {
            if (socketChannel.isConnectionPending()) {
                System.out.println("接收到了请求\n");
                socketChannel.write(buffer);
            } else {
                System.out.print(".");
                Thread.sleep(200);
            }
        }
    }
}

/**
 * 从Java NIO 书上复制过来的
 */
class Example_from_Java_Nio_book {
    public static void main(String[] argv) throws Exception {
        String host = "localhost";
        int port = 1234;

        //
        SocketChannel sc = SocketChannel.open();
        // 手动置为非阻塞模式
        sc.configureBlocking(false);
        // 连接到服务端的host:port地址
        sc.connect(new InetSocketAddress(host, port));

        // 判断是否完成connect连接
        while (!sc.finishConnect()) {
            doSomethingUseful();
        }

        // finishConnect = true 表示与服务端建立了connect连接
        System.out.println("connection established"); // Do something with the connected socket
        // The SocketChannel is still nonblocking
        sc.close();
    }

    private static void doSomethingUseful() {
        System.out.println("doing something useless");
    }
}