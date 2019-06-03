package com.mytest.nio;

import com.mytest.io.Util;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    @Test
    void check_file_append() {
        String p = NioTest.path;
        try(FileChannel c =
                new RandomAccessFile(p, "rw").getChannel()) {

            c.position(c.size());

            CharBuffer buffer = CharBuffer.wrap("HelloNio");
            buffer.flip();
            // 等效
            buffer.limit(buffer.capacity());// 使用了wrap, 使capacity == 内容长度
            buffer.position(0);
            System.out.println("补充的内容是: " + buffer);

            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer.toString().getBytes());
            Util.printPosLim("ByteBuffer wrap", byteBuffer);
            // byteBuffer.position(0);
            // byteBuffer.limit(byteBuffer.capacity());
            c.write(byteBuffer);
            Util.printPosLim("ByteBuffer after write", byteBuffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_read_file_by_filechannel() {
        String p = NioTest.path;
        try(FileChannel c =
                    new RandomAccessFile(p, "rw").getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(10);

            while (c.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining())
                    System.out.print((char)buffer.get());
                // System.out.println("\n");

                buffer.clear();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
