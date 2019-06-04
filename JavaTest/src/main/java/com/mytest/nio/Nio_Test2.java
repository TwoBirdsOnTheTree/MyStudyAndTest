package com.mytest.nio;

import com.mytest.io.Util;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class Nio_Test2 {
    public static String path = "C:\\Users\\sunch\\Desktop\\test.txt";

    public static void main(String[] args) throws Exception {
        Nio_Test2 t = new Nio_Test2();
        t.test1();
    }

    void test1() throws Exception {
        try (FileChannel channel =
                     new FileInputStream(path).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            System.out.println("初始分配后, position: " + buffer.position() + ", limit: " + buffer.limit());

            int read;
            while( (read = channel.read(buffer)) != -1) {

                System.out.println("读取后, position: " + buffer.position() + ", limit: " + buffer.limit());

                buffer.flip();

                System.out.println("flip后, position: " + buffer.position() + ", limit: " + buffer.limit());

                // System.out.println(read);
                System.out.println(new String(buffer.array(), 0, read, "UTF-8"));

                System.out.println("显示后, position: " + buffer.position() + ", limit: " + buffer.limit());

                buffer.clear();

                System.out.println("clear后, position: " + buffer.position() + ", limit: " + buffer.limit());
            }
        };
    }

    @Test
    void test_position_limit_meaning() {
        CharBuffer b = CharBuffer.allocate(100);
        b.put("HelloHowAreYou");
        Util.printPosLim("put", b);
        System.out.println(b.toString());

        b.position(0);
        System.out.println(b.toString());

        b.position(5);
        System.out.println(b);

        b.position(5);
        b.limit(8);
        System.out.println(b);

        // limit < position
        // exception
        /*b.limit(5);
        b.position(8);
        System.out.println(b);*/
    }
}
