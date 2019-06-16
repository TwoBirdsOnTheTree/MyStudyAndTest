package com.mytest.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Selector_wakeup_test {
    public static void main(String[] args) throws Exception {
        Selector_wakeup_test t = new Selector_wakeup_test();
        t.new wakeup().start();
    }

    class wakeup {
        void start() throws Exception {
            Selector selector = Selector.open();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("5s后Selector.wakeup...");
                    selector.wakeup();
                }
            }, 1000 * 5);


            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress("localhost", 1234));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                // 1. 有就绪的通道
                // 2. 被wakeup了
                System.out.println("Select.select执行有结果了, selectedKeys: " + selector.selectedKeys().size());

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                if (selectionKeys == null || selectionKeys.size() == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel serverChannelReady = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel acceptChannel = serverChannelReady.accept();
                        acceptChannel.write(ByteBuffer.wrap("Hello".getBytes()));
                        System.out.println("相应请求: " + serverChannelReady.getLocalAddress().toString());
                        acceptChannel.close();
                    }
                }
            }
        }
    }
}
