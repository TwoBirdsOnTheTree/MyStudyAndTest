package com.mytest.java8;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class StreamReduceTest {
    @Test
    void reduce() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        /**
         * reduce参数是BiOperator, 继承自BiFunction
         */

        Integer sum = list.stream().reduce((a, b) -> {
            return a + b;
        }).orElse(0);

        System.out.println(sum);
    }
}
