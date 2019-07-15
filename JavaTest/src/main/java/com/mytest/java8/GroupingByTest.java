package com.mytest.java8;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class GroupingByTest {
    @Test
    void test1() {
        class T {
            int id;

            int col1;
            int col2;

            T(int id, int col1, int col2) {
                this.id = id;
                this.col1 = col1;
                this.col2 = col2;
            }
        }
        List<T> list = Arrays.asList(
                new T(1, 1, 2),
                new T(2, 1, 1),
                new T(2, 3, 2),
                new T(3, 2, 1),
                new T(4, 2, 2),
                new T(5, 4, 1),
                new T(5, 2, 2),
                new T(5, 1, 1)
        );

        // 简单的分组
        // 类似SQL: select id, [] from list
        Map<Integer, List<T>> collect = list.stream()
                .collect(Collectors.groupingBy(
                        k -> k.id
                ));

        // 分组后
        // 类似SQL: select id, sum(col1) from list
        // Api: groupingBy(Function, Collector)
        Map<Integer, Long> collect1 = list.stream()
                .collect(Collectors.groupingBy(
                        k -> k.id,
                        Collectors.summingLong(m -> m.col1)
                ));

        // 同
        // 类似SQL: select id, sum(col1) from list
        // 和 `groupingBy(Function, Collector)` 区别仅仅是 `自定义了结果xxxxx`
        TreeMap<Integer, Long> collect2 = list.stream()
                .collect(Collectors.groupingBy(
                        k -> k.id,
                        TreeMap::new,
                        Collectors.summingLong(m -> m.col1)
                ));

        // Collectors.groupingBy + Collectors.collectingAndThen
        // 类似SQL: select id, sum(col1) / sum(col2) from list
        // 思路是: 利用`Collectors.collectingAndThen`
        Map<Integer, Long> collect3 = list.stream()
                .collect(Collectors.groupingBy(
                        k -> k.id,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                subList -> {
                                    long col1 = subList.stream().mapToLong(m -> m.col1).sum();
                                    long col2 = subList.stream().mapToLong(m -> m.col2).sum();
                                    return col1 / col2;
                                }
                        )
                ));
    }
}
