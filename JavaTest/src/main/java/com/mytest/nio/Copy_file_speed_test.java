package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * FileChannel.transferTo 反而比较慢
 * BIO设置缓存数组为8192(参考Files类)比较快
 * MappedByteBuffer的也快, 差不多
 *
 * 这个问题应该不需要再测试了, 一般 Files.copyTo 速度都不错
 * https://baptiste-wicht.com/posts/2010/08/file-copy-in-java-benchmark.html
 */
public class Copy_file_speed_test {
    // 14.9M
    private static String source_file = "C:\\Users\\Stark\\Desktop\\test.log";
    private static String target_file = "C:\\Users\\Stark\\Desktop\\copy.log";

    @Test
    void copy_by_channel_to_channel() throws Exception {
        long start = System.currentTimeMillis();

        Path source = Paths.get(source_file);
        Path target = Paths.get(target_file);
        File targetFile = target.toFile();
        if (targetFile.exists()) {
            targetFile.delete();
        }
        targetFile.createNewFile();

        FileChannel read = FileChannel.open(source, StandardOpenOption.READ);
        FileChannel write = FileChannel.open(target, StandardOpenOption.WRITE);

        read.transferTo(0, read.size(), write);

        write.force(false);

        // 83ms
        System.out.println("花费时间: " + (System.currentTimeMillis() - start));
    }

    @Test
    void copy_by_mappedByteBuffer() throws Exception {
        long start = System.currentTimeMillis();

        Path source = Paths.get(source_file);
        Path target = Paths.get(target_file);
        File targetFile = target.toFile();
        if (targetFile.exists()) {
            targetFile.delete();
        }
        targetFile.createNewFile();

        FileChannel read = FileChannel.open(source, StandardOpenOption.READ, StandardOpenOption.WRITE);
        MappedByteBuffer mappedByteBuffer = read.map(FileChannel.MapMode.READ_WRITE, 0, read.size());

        FileChannel write = FileChannel.open(target, StandardOpenOption.READ, StandardOpenOption.WRITE);
        MappedByteBuffer writeMappedByteBuffer = write.map(FileChannel.MapMode.READ_WRITE, 0, read.size());

        writeMappedByteBuffer.put(mappedByteBuffer);

        // 30ms
        System.out.println("花费时间: " + (System.currentTimeMillis() - start));
    }

    @Test
    void copy_by_files() throws Exception {
        long start = System.currentTimeMillis();

        File source = new File(source_file);
        File target = new File(target_file);
        if (target.exists()) {
            target.delete();
        }
        target.createNewFile();

        FileInputStream is = new FileInputStream(source);
        FileOutputStream os = new FileOutputStream(target);

        byte[] buffer = new byte[8192];

        int length;
        while ((length = is.read(buffer)) != -1) {
            os.write(buffer, 0, length);
        }

        os.flush();
        os.close();

        // 30ms
        System.out.println("花费时间: " + (System.currentTimeMillis() - start));
    }
}
