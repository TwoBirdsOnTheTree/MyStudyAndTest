package com.mytest.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Util {
    public static String path = null;
    public static String writePath = null;

    static {
        // String filePath = Util.class.getClassLoader().getResource("test.txt").getFile();
        // path = new File(filePath).getAbsolutePath();

        // String writeFilePath = Util.class.getClassLoader().getResource("test.txt").getFile();
        // writePath = new File(writeFilePath).getAbsolutePath();
    }

    // 获取classpath
    public static void main(String[] args) {
        String classPath = Thread.currentThread()
                .getContextClassLoader().getResource("").getPath();
        System.out.println();
        System.out.println(classPath);

        File file = new File(classPath + "/test.txt");
        System.out.println();
        System.out.println(file.exists());

        System.out.println();
        System.out.println(System.getProperty("java.class.path"));

        System.out.println();
        System.out.println(new File(".").getAbsolutePath());
    }

    @Test
    void get_classpath() {
        String path1 = Util.class.getResource("/").toString();
        File file = new File(path1);
        System.out.println(path1);
        System.out.println(file.exists());

        System.out.println();
        String path2 = Paths.get("").toFile().getAbsolutePath();
        System.out.println(path2);

        /*String path3 = getClass().getClassLoader().getResource("/test.txt").getFile();
        System.out.println();
        System.out.println(path3);*/

        System.out.println();
        System.out.println(Paths.get("text.txt").toFile().getAbsolutePath());

        //TODO 这个junit下, 也可以正常获取到classpath
        System.out.println();
        String path = Util.class.getClassLoader().getResource("test.txt").getFile();
        System.out.println(path);
        System.out.println(new File(
                path
        ).getAbsolutePath());
    }

    /**
     * 消除Thread.sleep的try catch结构
     * @param sleepMilliSeconds 。。
     */
    public static void sleep(Integer sleepMilliSeconds) {
        try {
            if (Objects.nonNull(sleepMilliSeconds)) {
                Thread.sleep(sleepMilliSeconds);
            }
        } catch (InterruptedException e) {
            System.out.println("线程中断了");
            e.printStackTrace();
        }
    }

    public static void print(String s) {
        System.out.println(s);
    }
}
