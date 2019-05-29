package com.mytest.io;

import org.junit.jupiter.api.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class Nio_ByteBuffer_Test {
    @Test
    void test_buffer_position_limit_change() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println("初始分配后, position: " + buffer.position() + ", limit: " + buffer.limit());

        buffer.put(new byte[]{1, 2, 3});
        System.out.println("put后, position: " + buffer.position() + ", limit: " + buffer.limit());

        // 这个时候的position代表了 buffer内容的长度
        int length = buffer.position();

        buffer.clear();
        buffer.limit(length);
        // System.out.println("clear后, position: " + buffer.position() + ", limit: " + buffer.limit());

        // buffer.flip();
        // System.out.println("flip后, position: " + buffer.position() + ", limit: " + buffer.limit());

        while (buffer.hasRemaining())
            System.out.println(buffer.get());
        // System.out.println(new String(buffer.array()));
    }

    @Test
    void test_rewind() {
        char[] chars = "MyTest".toCharArray();

        ByteBuffer bb = ByteBuffer.allocate(100);
        CharBuffer cb = bb.asCharBuffer();

        cb.put(chars);
        Util.printPosLim("put", cb);

        Buffer rewind = cb.rewind();
        Util.printPosLim("rewind", cb);

        System.out.println(rewind);
        Util.printPosLim("读取 rewind", cb);

        System.out.println(cb.toString());

        // unsupported ex
        // bytebuffer to charbuffer throw ex
        // System.out.println(new String(cb.array()));
    }

    @Test
    void test_why_charbuffer_array_throw_not_supported() {
        CharBuffer b = CharBuffer.allocate(100);
        b.put("Hello".toCharArray());

        // not throw notsupported ex
        System.out.println(new String(b.array()));
    }
}
