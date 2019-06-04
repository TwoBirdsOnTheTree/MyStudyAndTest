package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * System.out.println 会拖慢速度, 因此修改为StringBuffer测试
 *
 * 通过 while(buffer.hasRemaining()) buffer.get() 速度太慢了
 *
 * 通过 buffer.array() 速度很快, 比bio的快一点
 *
 * 未测试直接缓冲器
 */
public class Nio_Is_Faster_Than_IO_Test {
    // 17.2M
    private static String read_file = "C:\\Users\\Stark\\Desktop\\test.log";

    @Test
    void test_read_by_nio_filechannel() throws Exception {
        long start = System.currentTimeMillis();

        FileChannel channel = new FileInputStream(read_file).getChannel();
        // ByteBuffer buffer = ByteBuffer.allocate(1024);
        // ByteBuffer buffer = ByteBuffer.allocate(5096);
        ByteBuffer buffer = ByteBuffer.allocate(10240);

        StringBuffer sb = new StringBuffer();
        int length;
        while ((length = channel.read(buffer)) != -1) {
            buffer.flip();
            //TODO 通过while buffer.get() 读也太慢了
            // while (buffer.hasRemaining())
            //System.out.print((char) buffer.get());
            // 即使修改为StringBuffer也很慢
            // sb.append((char)buffer.get());

            //TODO 这样也比while buffer.get()快, 和buffer.array()差不多
            // byte[] bytes = new byte[buffer.remaining()];
            // buffer.get(bytes);
            // sb.append(new String(bytes));

            //TODO 通过buffer.array()读
            sb.append(new String(buffer.array(), 0, length));

            buffer.clear();
        }

        // 通过 while buffer.get(): 35s
        // 通过 Buffer.array(): 458ms / 501ms / 472ms (buffer: 10m)
        // 通过 Buffer.array(): 517ms (buffer: 5m)
        // 通过 Buffer.array(): 694ms (buffer: 1m)
        // 通过 Buffer.array() + StrinBuffer, buffer: 10m, 花费: 148ms
        System.out.println("时间是: " + (System.currentTimeMillis() - start));
    }

    @Test
    void test_read_by_bufferedreader() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(read_file))) {
            long start = System.currentTimeMillis();
            StringBuffer sb = new StringBuffer();

            String temp;
            while ((temp = br.readLine()) != null) {
                // System.out.println(temp);
                sb.append(temp);
            }

            // 746ms
            // 修改为StringBuffer后: 181ms
            System.out.println("时间是: " + (System.currentTimeMillis() - start));
        }
    }
}
