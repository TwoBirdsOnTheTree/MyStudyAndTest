package com.mytest.nio;

import org.junit.jupiter.api.Test;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Use_channel_to_channel_copy_file {
    // 17.2M
    private static String source_file = "C:\\Users\\Stark\\Desktop\\test.log";
    private static String target_file = "C:\\Users\\Stark\\Desktop\\copy.log";

    @Test
    void test() throws Exception {
        FileChannel read = FileChannel.open(Paths.get(source_file), StandardOpenOption.READ);
        Path targetFile = Paths.get(target_file);
        if (!targetFile.toFile().exists()) {
            targetFile.toFile().createNewFile();
        }
        FileChannel write = FileChannel.open(targetFile, StandardOpenOption.WRITE);

        // 将读入通道 transferTo 写入通道
        // 一步就实现了复制文件
        read.transferTo(0, read.size(), write);

        read.force(false);
    }
}
