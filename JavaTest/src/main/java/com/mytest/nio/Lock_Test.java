package com.mytest.nio;

import com.mytest.util.Util;

import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Lock_Test {
    public static void main(String[] args) throws Exception {
        Lock_Test t = new Lock_Test();
        t.test();
    }

    void test() throws Exception {
        Path path = Paths.get(Util.path);
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE);

        FileLock lock = channel.tryLock();

        FileChannel lockChannel = lock.channel();

        System.out.println(lock.isValid());
        System.out.println(lock.isShared());

        lock.release();
    }
}
