package com.mytest.java8;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamPerformance {

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
