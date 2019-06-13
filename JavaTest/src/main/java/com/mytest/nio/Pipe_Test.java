package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class Pipe_Test {
    public static void main(String[] args) throws Exception{
        new Pipe_Test().test();
    }

    void test() throws Exception {
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sink = pipe.sink();
        Pipe.SourceChannel source = pipe.source();

        source(source);
        sink(sink);
    }

    void sink(Pipe.SinkChannel sinkChannel) throws Exception {
        Thread.sleep(1000);

        System.out.println("sinkchannel 发送数据1");
        sinkChannel.write(ByteBuffer.wrap("你好啊Pipe1111111111".getBytes()));

        Thread.sleep(1000);

        System.out.println("sinkchannel 发送数据2");
        sinkChannel.write(ByteBuffer.wrap("你好啊Pipe2222222222".getBytes()));
    }

    void source(Pipe.SourceChannel sourceChannel) throws Exception {
        sourceChannel.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);

        new Thread(() -> {
            try {
                while (true) {
                    sourceChannel.read(byteBuffer);
                    if (byteBuffer.position() != 0) {
                        byteBuffer.flip();
                        System.out.println("接收到: " +
                                new String(byteBuffer.array(), 0, byteBuffer.remaining()));
                        byteBuffer.clear();
                    } else {
                        System.out.println('.');
                        Thread.sleep(200);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
