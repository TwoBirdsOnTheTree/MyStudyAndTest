package com.mytest.java8;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stream的性能验证
 */
public class StreamPerformance {

    /**
     * 1. 并行初始化需要一点时间 (很少)
     * 2. 如果循环的每一步都耗时的话, 用并行可以大幅减少时间, 减少了大概非并行的 list.size() 倍的时间
     * 3. 如果循环的每一步不是非常耗时的话, 并行可能反而慢
     *
     * 这里验证的数据量不大
     */
    @Test
    void parallel_test() {
        // 单个循环项下, 验证并行本身花费时间
        // List<String> list = Arrays.asList("a");
        // 验证多个任务并行的情况
        List<String> list = Arrays.asList("a", "b-c-i", "d-e-f", "g-h");

        long start1 = System.currentTimeMillis();
        List<String> toList = new ArrayList<>();
        for (String str : list) {
            toList.addAll(this.mockService(str));
        }
        System.out.println(toList);
        System.out.println("花费时间: " + (System.currentTimeMillis() - start1));

        long start2 = System.currentTimeMillis();
        List<String> toList2 = list.parallelStream()
                .flatMap(m -> this.mockService(m).stream())
                .collect(Collectors.toList());
        System.out.println(toList2);
        System.out.println("花费时间: " + (System.currentTimeMillis() - start2));
    }

    // 模拟查询
    private List<String> mockService(String str) {
        try {
            // 模拟不耗时时, 注释掉
            // Thread.sleep(200);
            // 模拟耗时
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Arrays.asList(str.split("-"));
    }

    /**
     * 验证 stream.map的性能
     */
    @Test
    void map_test() {
        class Std {
            Integer id;
            String name;

            Std(Integer id, String name) {
                this.id = id;
                this.name = name;
            }
        }
        List<Std> list = new ArrayList<Std>() {{
            IntStream.range(0, 5000).forEach(f -> add(new Std(f, "学生 - " + f)));
        }};

        long start1 = System.currentTimeMillis();
        Map<Integer, List<Std>> collect = list.stream().collect(Collectors.groupingBy(g -> g.id));
        Map<String, List<Std>> collect2 = list.stream().collect(Collectors.groupingBy(g -> g.name));
        System.out.println("1: " + (System.currentTimeMillis() - start1));

        long start2 = System.currentTimeMillis();
        Map<Integer, List<Std>> collect3 = new HashMap<>();
        Map<String, List<Std>> collect4 = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Std std = list.get(i);
            if (!collect3.containsKey(std.id))
                collect3.put(std.id, new ArrayList<>());
            collect3.get(std.id).add(std);

            if (!collect4.containsKey(std.name))
                collect4.put(std.name, new ArrayList<>());
            collect4.get(std.name).add(std);
        }
        System.out.println("2: " + (System.currentTimeMillis() - start2));
    }
}
