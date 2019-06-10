package com.mytest.nio;

import com.mytest.util.Util;
import org.junit.jupiter.api.Test;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MappedByteBufferTest {
    @Test
    void test() throws Exception {
        FileChannel channel = FileChannel.open(Paths.get(Util.path), StandardOpenOption.WRITE, StandardOpenOption.READ);

        MappedByteBuffer mapByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());

        byte[] bytes = new byte[mapByteBuffer.capacity()];
        mapByteBuffer.get(bytes);

        System.out.println(new String(bytes));
    }
}
