package com.mytest.nio;

import com.mytest.util.Util;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.ByteBuffer;
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

    @Test
    void speed_test_vs_array() throws Exception {
        long start = System.currentTimeMillis();
        FileChannel channel = FileChannel.open(Paths.get(Util.path),
                StandardOpenOption.WRITE, StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
        channel.read(buffer);

        StringBuilder builder = new StringBuilder();
        builder.append(new String(buffer.array()));

        // 148ms
        System.out.println("花费时间: " + (System.currentTimeMillis() - start));
    }

    @Test
    void speed_test_vs_array_by_mappedbytebuffer() throws Exception {
        long start = System.currentTimeMillis();
        FileChannel channel = FileChannel.open(Paths.get(Util.path),
                StandardOpenOption.WRITE, StandardOpenOption.READ);

        MappedByteBuffer buffer =
                channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);

        StringBuilder builder = new StringBuilder();
        builder.append(new String(bytes));

        // 140ms 左右
        System.out.println("花费时间: " + (System.currentTimeMillis() - start));
    }

    @Test
    void test_MapMode_Write() throws Exception {
        String path = "C:\\Users\\sunch\\Desktop\\write.txt";
        FileChannel channel = FileChannel.open(Paths.get(path),
                StandardOpenOption.WRITE, StandardOpenOption.READ);

        MappedByteBuffer buffer =
                channel.map(FileChannel.MapMode.READ_WRITE, 0,
                        /*
                        * 不能是: channel.size(), 文件初始时size是0,
                        * 这样mappedByteBuffer.put时就是异常
                        * */
                        1000);

        channel.close();

        buffer.put("你好吗".getBytes());
    }

    @Test
    void test_MapMode_Private() throws Exception {
        String path = "C:\\Users\\sunch\\Desktop\\write.txt";
        FileChannel channel = FileChannel.open(Paths.get(path),
                StandardOpenOption.WRITE, StandardOpenOption.READ);

        /*ByteBuffer oldBuffer = ByteBuffer.wrap("你好啊".getBytes());
        channel.write(oldBuffer);
        channel.force(false);*/

        MappedByteBuffer buffer =
                channel.map(FileChannel.MapMode.PRIVATE, 0, "你好吗".getBytes().length);

        buffer.put("你好吗".getBytes());
    }

    @Test
    void can_not_write_test() throws Exception {
        String path = "C:\\Users\\sunch\\Desktop\\write.txt";
        FileChannel channel = FileChannel.open(Paths.get(path),
                StandardOpenOption.WRITE, StandardOpenOption.READ);

        ByteBuffer oldBuffer = ByteBuffer.wrap("你好啊".getBytes());
        channel.write(oldBuffer);
        channel.force(false);
    }
}
