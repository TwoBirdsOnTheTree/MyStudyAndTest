package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * Selector:
 * FileChannel 不是 非阻塞的??? 所以不能使用Selector?
 */
public class SelectorTest {
    @Test
    void simple_test() throws Exception {

        Selector selector = Selector.open();

        //TODO not init
        SelectableChannel channel = null;

        // 将SelectableChannel注册到Selector中
        // 返回选择键(SelectionKey)
        SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);

    }


    @Test
    void api_test() throws Exception{
        Selector.open();

        ServerSocketChannel channel = ServerSocketChannel.open();
        int validOps = channel.validOps();
        System.out.println(validOps);

        System.out.println();
        System.out.println(SelectionKey.OP_READ);
        System.out.println(SelectionKey.OP_WRITE);
        System.out.println(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

         System.out.println(5 | 3);
    }
}
