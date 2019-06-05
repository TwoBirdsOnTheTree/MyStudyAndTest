package com.mytest.nio;

import com.mytest.util.Util;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class View_Buffer_Test {

    @Test
    void test() {
        byte[] bytes = new byte[]{0, 'H', 0, 'e', 0, 'l', 0, 'l', 0, 'o'};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        while (byteBuffer.hasRemaining())
            System.out.print(byteBuffer.get());

        System.out.println();

        // 先clear, 不然转换的charbuffer没有数据
        byteBuffer.clear();

        CharBuffer charBuffer = byteBuffer.asCharBuffer();
        System.out.println("asCharBuffer length: " + charBuffer.remaining());
        while (charBuffer.hasRemaining())
            System.out.print(charBuffer.get());
    }

    /**
     * 中文乱码啊.....
     */
    @Test
    void test_chinese() {
        byte[] bytes = "你好".getBytes();
        for (byte b : bytes) {
            System.out.print(b);
        }
        // bytes = new byte[]{0, bytes[0], 0, bytes[1], 0, bytes[2], 0, bytes[3], 0, bytes[4], 0, bytes[5]};
        System.out.println();
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        while (byteBuffer.hasRemaining())
            System.out.print((char) byteBuffer.get());

        byteBuffer.clear();
        CharBuffer charBuffer = byteBuffer.asCharBuffer();

        while (charBuffer.hasRemaining())
            System.out.println(charBuffer.get());
    }

    /**
     * 好像应该这么用, 原缓冲器不修改数据, 使用试图缓冲器修改/读取数据
     */
    @Test
    void test_use_view_buffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        CharBuffer charBuffer = byteBuffer.asCharBuffer();

        charBuffer.put("你好");

        charBuffer.clear();
        while (charBuffer.hasRemaining())
            System.out.print(charBuffer.get());

        System.out.println();

        byteBuffer.clear();
        while (byteBuffer.hasRemaining()) {
            System.out.print(byteBuffer.get());
        }
    }

    /**
     * 读取文件时, asCharBuffer也会乱啊.
     * 感觉, 原缓冲器里面就不应该有数据, 否则生成的视图缓冲器容易乱码. 应该由视图缓冲器写入数据.
     * @throws Exception
     */
    @Test
    void read_chinese_file() throws Exception {
        try (FileChannel channel = new FileInputStream(Util.path).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

            channel.read(buffer);
            buffer.flip();

            CharBuffer charBuffer = buffer.asCharBuffer();
            while (charBuffer.hasRemaining())
                System.out.print(charBuffer.get());

            // 使用string
            System.out.println();
            String str = new String(buffer.array(), 0, buffer.remaining());
            System.out.println(str);
        }
    }

    @Test
    void test11() {
        byte h = 'h';
        System.out.println(h);
    }
}
