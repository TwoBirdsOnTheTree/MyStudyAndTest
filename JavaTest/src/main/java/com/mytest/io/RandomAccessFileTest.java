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
            while ((temp = accessFile.readLine()) != null ) {
                System.out.println(temp);
            }
        }
    }
}
