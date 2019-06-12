package com.mytest.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockSocketTest {
    public static void main(String[] args) throws Exception {
        BlockSocketTest t = new BlockSocketTest();

        t.test1();
    }

    void test1() throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("localhost", 1234));

        while (true) {
            System.out.println("accept!");

            // 会阻塞, 直到接收到请求
            Socket acceptSocket = serverSocket.accept();

            System.out.println("接收到请求");

            acceptSocket.getOutputStream().write("Hello Socket".getBytes());

            acceptSocket.close();
        }
    }
}
