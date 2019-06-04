package com.mytest.io;

import com.mytest.util.Util;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileTest {

    @Test
    void test() throws IOException {
        try (RandomAccessFile accessFile = new RandomAccessFile(Util.path, "r")) {
            String temp;
            while ((temp = accessFile.readLine()) != null) {
                System.out.println(temp);
            }
        }
    }

    @Test
    void append_file_by_seek() throws Exception {
        try (RandomAccessFile accessFile = new RandomAccessFile(Util.path, "r")) {
            // 设置文件指针到文件的底部 !!
            accessFile.seek(accessFile.length());
            // 这时候再写入, 就是在文件底部补充内容了
            accessFile.write("hello".getBytes());
        }
    }
}
