package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;
import java.util.stream.IntStream;

public class DuplicateBufferTest {
    @Test
    void duplicate() {
        CharBuffer buffer = CharBuffer.wrap("HelloNio".toCharArray());

        CharBuffer duplicate = buffer.duplicate();

        // duplicate读取
        duplicate.position(0);
        IntStream.range(0, 5).forEach(i -> {
            System.out.print(duplicate.get());
        });
        System.out.println("\n");

        // 修改duplicate, 会影响原缓冲器吗
        duplicate.position(0);
        duplicate.put('P');
        buffer.position(0);
        System.out.print(buffer.get());
        System.out.print(buffer.get());
        System.out.print(buffer.get());
        System.out.print(buffer.get());
        System.out.print(buffer.get());
        System.out.println("\n");

        // 修改原缓冲器, 会影响duplicate吗
        buffer.rewind();
        duplicate.rewind();
        buffer.put('H');// 还原
        IntStream.range(0, 5).forEach(i -> {
            System.out.print(duplicate.get());
        });
        System.out.println("\n");

        // 只读复制缓冲器
        CharBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        readOnlyBuffer.position(0);
        char[] chars = new char[readOnlyBuffer.remaining()];
        readOnlyBuffer.get(chars);
        System.out.println("readOnlyBuffer: " + new String(chars));
        try {
            readOnlyBuffer.put('d');// 异常
        } catch (Exception e) {
            e.printStackTrace();
        }

        // slice
        buffer.clear();
        buffer.position(3).limit(5);
        CharBuffer slice = buffer.slice();
        IntStream.range(0, slice.remaining()).forEach(i -> System.out.print(slice.get()));
        System.out.println("\nslice arrayOffset: " + slice.arrayOffset());
    }
}
