package com.mytest.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketOption;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class BioMultiThreadTest implements Runnable {
    private Socket clientSocket;
    private boolean useThread = true;

    public BioMultiThreadTest(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static void main(String[] args) throws IOException {
        new BioMultiThreadTest(null).startServer();
    }

    public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        // 监听localhost即监听本机ipv4地址
        // 但是客户端默认是发送IPv6地址请求的, 所以IPv6地址请求就会收到rst的tcp响应
        // serverSocket.bind(new InetSocketAddress("localhost", 888));
        serverSocket.bind(new InetSocketAddress(888)); // 监听所有地址, 包括IPv6
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("client socket accepted");
            if (useThread) {
                // 多线程执行
                // clientSocket.setSoTimeout(100000);
                // clientSocket.setKeepAlive(true);
                // clientSocket.setSoLinger(true, 100);
                new Thread(new BioMultiThreadTest(clientSocket)).start();
            } else {
                // 单线程执行
                clientSocket.setSoTimeout(100000);
                int soTimeout = clientSocket.getSoTimeout();
                System.out.println("soTimeout: " + soTimeout);
                this.clientSocket = clientSocket;
                this.run();
            }
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("run start");
            String responseString = "HelloBioWith - " + new Random().nextInt(1000);
            InputStream inputStream = clientSocket.getInputStream();

            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(("HTTP/1.1 200\r\n" +
                    "Content-Type: text/html;charset=UTF-8\r\n" +
                    // "Connection: keep-alive\r\n" +
                    "Content-Length: " + responseString.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                    "\r\n" +
                    responseString
            ).getBytes());
            outputStream.flush();
            // clientSocket.shutdownInput();
            //TODO ? shutdownOutput ?? 半关闭...学习中
            clientSocket.shutdownOutput();
            //TODO 不需要关闭!!! ??? 发送玩就自动关闭了... // 错误, 会导致socket close_wait
            // outputStream.close();
            // inputStream.close();
            // clientSocket.close();
            Thread.sleep(20 * 1000);
            System.out.println("run end, socket closed: " + clientSocket.isClosed() + ", socket isConnected: " + clientSocket.isConnected());
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
