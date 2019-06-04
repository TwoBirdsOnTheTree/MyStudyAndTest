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
 * 通过 buffer.array() 速度很快, 整体比bio的快一点, 几十毫秒吧
 *
 * 未测试直接缓冲器
 * 测试大量写?
 */
public class Nio_Is_Faster_Than_IO_Test {
    // 17.2M
    private static String read_file = "C:\\Users\\Stark\\Desktop\\test.log";

    @Test
    void test_read_by_nio_filechannel() throws Exception {
        long start = System.currentTimeMillis();

        FileChannel channel = new FileInputStream(read_file).getChannel();
        // 修改buffer capacity
        // ByteBuffer buffer = ByteBuffer.allocate(1024);
        // ByteBuffer buffer = ByteBuffer.allocate(5096);
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

        StringBuffer sb = new StringBuffer();
        int length;
        while ((length = channel.read(buffer)) != -1) {
            buffer.flip();
            // 1.
            //TODO 通过while buffer.get() 读也太慢了
            // while (buffer.hasRemaining())
            //System.out.print((char) buffer.get());
            // 即使修改为StringBuffer也很慢
            // sb.append((char)buffer.get());

            // 2.
            //TODO 这样也比while buffer.get()快, 和buffer.array()差不多
            // byte[] bytes = new byte[buffer.remaining()];
            // buffer.get(bytes);
            // sb.append(new String(bytes));

            // 3.
            //TODO 通过buffer.array()读
            sb.append(new String(buffer.array(), 0, length));

            buffer.clear();
        }

        // 通过System.out.println
        //     通过 while buffer.get(): 35s
        //     通过 Buffer.array(): 458ms / 501ms / 472ms (buffer: 1k)
        //     通过 Buffer.array(): 517ms (buffer: 5k)
        //     通过 Buffer.array(): 694ms (buffer: 1m)
        // 通过 StringBuilder
        //     通过 Buffer.array(), buffer: 1m, 花费: 114ms / 192ms / 188ms / 152ms / 145ms
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
            // 修改为StringBuffer后: 181ms / 165ms / 208ms / 174ms
            System.out.println("时间是: " + (System.currentTimeMillis() - start));
        }
    }
}
