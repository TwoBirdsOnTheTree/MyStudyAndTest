package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

public class Is_ByteBuffer_loop_get_slow_test {
    @Test
    void test() {
        StringBuffer stringBuffer = new StringBuffer();
        String str = "1234567890";
        IntStream.range(0, 1024 * 1024 * 5).forEach(i -> {
            stringBuffer.append(str);
        });

        ByteBuffer buffer = ByteBuffer.wrap(stringBuffer.toString().getBytes());

        /**
         * 这种好慢啊, 246ms
         */
        long start1 = System.currentTimeMillis();
        // buffer.flip();
        StringBuffer get = new StringBuffer();
        while (buffer.hasRemaining()) {
            char c = (char) buffer.get();
            get.append(c);
        }
        System.out.println("循环get花费时间: " + (System.currentTimeMillis() - start1));
        // System.out.println(get.toString());


        /**
         * 一般是最快的, 46ms
         */
        System.out.println();
        long start2 = System.currentTimeMillis();
        buffer.flip();
        StringBuffer get2 = new StringBuffer();
        byte[] array = buffer.array();
        get2.append(new String(array));
        System.out.println("array()花费时间: " + (System.currentTimeMillis() - start2));
        System.out.println(get2.toString());

        /**
         * get buffer不是越大越好,
         * 比循环get快, 比array()慢点
         * getBuffer,   1k:    57ms
         *              10k:    61ms
         *              100k:   60ms
         *              1m:     110ms
         */
        System.out.println();
        long start3 = System.currentTimeMillis();
        // buffer.flip(); // 通过array(), 不会造成pos移动..
        StringBuffer get3 = new StringBuffer();
        byte[] getBuffer = new byte[1024 * 1];
        while (buffer.hasRemaining()) {
            int length =
                    buffer.remaining() < getBuffer.length ? buffer.remaining() : getBuffer.length;
            buffer.get(getBuffer, 0, length);
            get3.append(new String(getBuffer, 0, length));
        }
        System.out.println("批量get花费时间: " + (System.currentTimeMillis() - start3));
        System.out.println(get3.toString());
    }

    @Test
    void test_get_array() {
        ByteBuffer buffer = ByteBuffer.wrap("hello".getBytes());
        byte[] arr = new byte[1024 * 1024];

        buffer.get(arr, 0, buffer.remaining() - 1);

        System.out.println(new String(arr));
    }
}
