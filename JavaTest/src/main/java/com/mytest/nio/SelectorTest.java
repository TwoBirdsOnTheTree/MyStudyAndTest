package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Selector:
 * FileChannel 不是 非阻塞的??? 所以不能使用Selector?
 */
public class SelectorTest {
    @Test
    void simple_test() throws Exception {

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress("localhost", 801));

            while (!socketChannel.finishConnect()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int length;
                while ((length = socketChannel.read(buffer)) != -1) {
                    System.out.print(new String(buffer.array(), 0, length));
                }
            }

            Selector selector = Selector.open();
        }
    }
}
