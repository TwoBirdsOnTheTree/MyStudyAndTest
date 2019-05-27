package com.mytest.io;

import java.nio.Buffer;

public class Util {
    public static <T extends Buffer> void printPosLim(String step, T b) {
        try {
            /*Stream.of(Buffer.class.getDeclaredMethods())
                    .filter(i -> i.getName().contains("mark"))
                    .forEach(i -> System.out.println(i.getName()));*/

            // Method mark = Buffer.class.getDeclaredMethod("markValue");
            // int markValue = (int) mark.invoke(b);

            System.out.println(step + "后"
                    + ", pos: " + b.position()
                    + ", lim: " + b.limit()
                    // + ", mar: " + markValue
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
