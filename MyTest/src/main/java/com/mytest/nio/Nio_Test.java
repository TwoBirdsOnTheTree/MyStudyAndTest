package com.mytest.nio;

import com.mytest.util.Util;
import org.junit.jupiter.api.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

class Nio_Test {

    @Test
    void my_first_test_nio_read() throws Exception {
        String path = "C:\\Users\\Stark\\Desktop\\test.txt";
        try (FileChannel channel =
                     new RandomAccessFile(path, "rw").getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(10);

            while (channel.read(buffer) != -1) {
                //TODO 一定需要flip, 否则, 无法读取到
                buffer.flip();

                while (buffer.hasRemaining())
                    System.out.print((char) buffer.get());

                //TODO 不clear的话, 会死循环(无限读)
                // 因为get读取结束后, position == limit, 导致read无法再往buffer中保存数据了
                buffer.clear();
            }
        }
    }

    @Test
    void test_filechannel_read() throws Exception {
        try (FileChannel channel
                     = new RandomAccessFile(Util.path, "rw").getChannel()) {

            ByteBuffer b = ByteBuffer.allocate(1000);

            b.position(5);
            b.limit(10);

            channel.read(b);
            System.out.println("b pos: " + b.position() + ", b.lim: " + b.limit());

            // 显示全部内容
            b.position(0);
            b.limit(b.capacity());
            while (b.hasRemaining()) {
                char get = (char) b.get();
                //TODO char的默认值是 '\0'
                if ('\0' == get)
                    get = '_';
                System.out.print(get);
            }

            //TODO 结果说明, read往buffer写入, 是写入pos到lim这一段
        }
    }
}
