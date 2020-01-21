package com.mytest.concurrent.java_concurrency_in_practice;

import org.junit.jupiter.api.Test;

public class ConcurrentHashMap_7_Test {
    @Test
    public void test_init() {
        int initialCapacity = 16;
        initialCapacity = 15;
        initialCapacity = 17;
        int concurrencyLevel = 16;
        // concurrencyLevel = 19;

        // Find power-of-two sizes best matching arguments
        int sshift = 0;
        int ssize = 1;
        while (ssize < concurrencyLevel) {
            ++sshift;
            // ssize <<= 1;
            ssize = ssize << 1;
        }
        int segmentShift = 32 - sshift;
        int segmentMask = ssize - 1;
        int c = initialCapacity / ssize;
        if (c * ssize < initialCapacity)
            ++c;
        int cap = 1;
        while (cap < c)
            cap <<= 1;

        System.out.println("sshift is: " + sshift);
        System.out.println("ssize is: " + ssize);
        System.out.println("segmentShift is: " + segmentShift);
        System.out.println("segmentMask is: " + segmentMask);
        System.out.println("cap is: " + cap);
    }
}
