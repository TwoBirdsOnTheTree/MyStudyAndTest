package com.mytest.io;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * java flush 和 close
 */
public class Java_io_flush_close_Test {
    public static void main(String[] args) throws Exception {
        Java_io_flush_close_Test t = new Java_io_flush_close_Test();
        t.test2();
    }

    void test2() {
        try(BufferedOutputStream os =
                new BufferedOutputStream(new FileOutputStream
                        ("C:\\Users\\sunch\\Desktop\\test.txt"));
                ) {

            IntStream.range(0, 200).forEach(i -> {
                try {
                    os.write(("_1234567890_" + i).getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            os.flush();
            // os.close();

            // Thread.sleep(15_000);

            // 防止try调用close方法
            System.exit(0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void test1() throws FileNotFoundException, InterruptedException {
        FileOutputStream os = new FileOutputStream("C:\\Users\\sunch\\Desktop\\test.txt");

        IntStream.range(0, 20).forEach(i -> {
            try {
                os.write(("_1234567890_" + i).getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(15_000);

        System.exit(0);
    }
}
