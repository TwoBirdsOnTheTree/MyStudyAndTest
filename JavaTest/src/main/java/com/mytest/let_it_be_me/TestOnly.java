package com.mytest.let_it_be_me;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TestOnly {

    @Test
    void test4() {
        List<String> s = new ArrayList<>(Arrays.asList("a", "b"));
        List<String> a = new ArrayList<>(Arrays.asList("c", "d"));
        /**
         * js 数组拼接方法是: concat, 对应java是addAll
         * js 使用concat后, 原数组是没有拼接的, 其返回值才是拼接后的结果, 这一点我经常忘
         * 而java 使用addAll后,
         */
        s.addAll(a);
    }

    @Test
    void test3() {
        Optional.ofNullable("ss")
                .ifPresent(item -> {
                    item = "dd";
                });
    }

    @Test
    void test1() {
        Predicate<Method> isSetFunc = p -> {
            return false;
        };

        Method m = null;
        boolean isSet = isSetFunc.test(m);
    }

    @Test
    void test2() {
        class JustTest {
            private String name;

            public String getName() {
                return this.name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        List<String> list = Arrays.asList("a", "b");
        List<JustTest> collect = list.stream()
                .map(item -> new JustTest() {{
                    setName(item);
                }})
                .collect(Collectors.toList());
        System.out.println(JSON.toJSONString(collect));
    }
}
