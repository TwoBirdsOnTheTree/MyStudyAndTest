package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Selector
 */
public class SelectorTest {

    /**
     * 执行通过 cmd:
     *      telnet localhost 1234
     *      telnet localhost 1235
     * @throws Exception
     */
    @Test
    void simple_test() throws Exception {

        Selector selector = Selector.open();

        // 将SelectableChannel注册到Selector中
        // 返回选择键(SelectionKey)
        SelectionKey selectionKey1 = this.getServerChannel(1234).get().register(selector, SelectionKey.OP_ACCEPT);
        SelectionKey selectionKey2 = this.getServerChannel(1235).get().register(selector, SelectionKey.OP_ACCEPT);

        test_by_block_select(selector);
    }

    void test_by_block_select(Selector selector) throws Exception {
        while (true) {
            System.out.println("开始执行Selector.select(), 会阻塞吗? 会!");
            // 阻塞, 直到
            int select = selector.select();

            System.out.println("有: " + select + "个channel就绪");
            if (select == 0) {// 阻塞的话这个判断就没有意义了
                System.out.println('_');
                continue;
            }
            // 当有就绪的Channel后
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            // 循环每个Channel
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                // 删除表示已经处理该Channel了, 这里还不太清楚
                iterator.remove();

                // 注册Selector时, interestOps 感兴趣的操作是: accept
                // 这里只处理了isAccept请求, 还有isReadable等
                if (selectionKey.isAcceptable()) {
                    // 如果是accept的话, 可以强转为 ServerSocketChannel
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    // accept返回的通道也可以注册Selector, interestOps 应该是read 或 write, 如果
                    // 注册到Selector, 那Selector应该 isReadable & isWriteable
                    // 可以中途不断地注册Selector
                    //TODO 既然这个channel已经就绪, 那这accept()返回结果应该不可能为空, 确定吗?
                    SocketChannel acceptChannel = channel.accept();
                    // ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                    // acceptChannel.read(buffer);
                    // 这里没有注册Selector, 直接响应请求了
                    acceptChannel.write(ByteBuffer.wrap("Hello".getBytes()));
                    acceptChannel.close();
                    System.out.println("响应请求, 对应端口是: " + channel.getLocalAddress().toString());
                }
            }
        }
    }

    private Supplier<ServerSocketChannel> getServerChannel(int port) {
        return () -> {
            try {
                ServerSocketChannel channel = ServerSocketChannel.open();
                // 手动置为none-blocking
                channel.configureBlocking(false);
                channel.bind(new InetSocketAddress("localhost", port));
                new Thread(() -> {

                }).start();
                return channel;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }


    @Test
    void api_test() throws Exception {
        Selector.open();

        ServerSocketChannel channel = ServerSocketChannel.open();
        int validOps = channel.validOps();
        // ServerSocketChannel的validOps是16, 表示只支持 SelectionKey.OP_ACCEPT
        // 即只支持 accept 操作
        System.out.println(validOps);

        System.out.println();
        System.out.println(SelectionKey.OP_READ);
        System.out.println(SelectionKey.OP_ACCEPT);
        System.out.println(SelectionKey.OP_READ | SelectionKey.OP_WRITE
                | SelectionKey.OP_CONNECT | SelectionKey.OP_ACCEPT);

    }
}
