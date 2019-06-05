package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class Data_View_test {
    @Test
    void test() {
        ByteBuffer buffer = ByteBuffer.wrap("H汉".getBytes());

        System.out.println(buffer.capacity());

        int i = 0;
        while (buffer.hasRemaining()) {
            System.out.print(" ," + i + ": " + buffer.get());
            i++;
        }

        buffer.clear();
        System.out.println();
        System.out.println(buffer.getChar());
        System.out.println(buffer.getChar());
    }

    @Test
    void test1() {
        ByteBuffer buffer = ByteBuffer.allocate(100);

        buffer.putChar('h');

        System.out.println(buffer.position());

        buffer.flip();

        while (buffer.hasRemaining())
            System.out.println(buffer.get());
    }

    @Test
    void test_chinese() {
        ByteBuffer buffer = ByteBuffer.allocate(100);

        buffer.putChar('哈');

        System.out.println(buffer.position());

    }

    @Test
    void ss() {
        System.out.println((char)72);
        System.out.println(String.valueOf('哈').getBytes().length);
        System.out.println(String.valueOf('的').getBytes().length);
    }
}
