package com.mytest.let_it_be_me;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestOnly {

    //TODO 额, 负数的负号, 在转字符串后, 和字符串"-"是一样的...
    @Test
    void just_be_sure() {
        String a = "one";
        Integer b = -888;
        Integer c = -1;
        String s = a + "-" + b + "-" + c;
        System.out.println(s);
        Stream.of(s.split("-")).forEach(f -> System.out.println(f));
    }

    // 倒序可以在foreach中同时删除
    @Test
    void test_foreach_delete() {
        List<String> strings = new ArrayList<>(Arrays.asList("a", "ab", "ac", "de"));
        for (int i = strings.size() - 1; i >= 0; i--) {
            System.out.println(strings.get(i));
            if ("ab".equals(strings.get(i))) {
                strings.remove(i);
            }
        }

        System.out.println(strings + "\n");

        ArrayList<String> strings2 = new ArrayList<>(Arrays.asList("a", "ab", "ac", "de"));

        // 写成方法
        reverseForeach(strings2, (i, v) -> {
            System.out.println(v);
            if ("ab".equals(v)) {
                strings2.remove(v);
                // strings2.remove(i.intValue());
            }
        });

        System.out.println(strings2);
    }

    /**
     * {@link TestOnly#forEach(Iterator, Consumer)}:<br>
     * 1. 顺序是正序, 2. 适用所有Iterator, 3. 需要先生成Iterator <br>
     * {@link TestOnly#reverseForeach(List, BiConsumer)}:<br>
     * 1. 倒序, 2. 仅适用List<br>
     *
     * @see TestOnly#forEach(Iterator, Consumer)
     */
    private <T> void reverseForeach(List<T> list, BiConsumer<Integer, T> consumer) {
        Objects.requireNonNull(list);
        for (int i = list.size() - 1; i >= 0; i--) {
            consumer.accept(i, list.get(i));
        }
    }

    @Test
    void not_sure() {
        class Change {
            String name;
        }
        Change c;
        IntStream.range(0, 5).forEach(i -> {
            // now sure
        });
    }

    @Test
    void little_comfused() {
        System.out.println((double) 6 / 12);
    }

    @Test
    void test_fastjson_toJSONString() {
        class InnerClass {
            public String str = "im string";
        }
        System.out.println(JSON.toJSONString(new InnerClass()));
    }

    @Test
    void test_groupingBy() {
        List<String> strings = Arrays.asList("a", "ab", "ac", "de");
    }

    @Test
    void test_java8_flatMap() {
        class Map {
            String id;
            List<String> ids;
        }
        Map a = new Map() {{
            id = "a";
            ids = Arrays.asList("b", "c");
        }};
        Map b = new Map() {{
            id = "d";
            ids = Arrays.asList("e", "f");
        }};

        List<String> flatMapResult = Stream.of(a, b).flatMap(i -> {
            List<String> ids = new ArrayList<>();
            ids.add(i.id);
            ids.addAll(i.ids);
            return ids.stream();
        }).collect(Collectors.toList());

        System.out.println(flatMapResult);
    }

    @Test
    void test_remove_list_by_forEach_Lambda() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Iterator<String> iterator = list.iterator();
        forEach(iterator, i -> {
            System.out.print(i);
            if ("c".equals(i))
                iterator.remove();
        });

        System.out.println("\n结果是: " + list);
    }

    /**
     * @see TestOnly#reverseForeach(List, BiConsumer)
     */
    <E> void forEach(Iterator<E> iterator, Consumer<? super E> action) {
        Objects.requireNonNull(iterator);
        Objects.requireNonNull(action);
        while (iterator.hasNext())
            action.accept(iterator.next());
    }

    @Test
    void test_remove_list_by_iterator() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()) {
            String i = iterator.next();
            System.out.print(i);

            if ("c".equals(i))
                iterator.remove();
        }

        System.out.println("\n" + list);
        list.add("c");

        // IllegalStateException Exception
        // 因为方法 forEachRemaining 被ArrayList覆写了...
        Iterator<String> iteratorForLambda = list.iterator();
        iteratorForLambda.forEachRemaining(i -> {
            System.out.print(i);
            if ("c".equals(i))
                iteratorForLambda.remove();
        });
    }

    @Test
    void list_remove() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        for (String i : list) {
            // ConcurrentModificationException
            if ("c".equals(i))
                list.remove(i);
        }
    }

    @Test
    void fastjson_parseObject_if_will_change_sort_test() {
        Map<String, String> map = new HashMap<String, String>() {{
            put("1", "1");
            put("2", "2");
            put("3", "3");
            put("4", "4");
        }};
        String str = JSON.toJSONString(map);
        System.out.println(str);
        JSONObject parse = JSON.parseObject(str);
        parse.entrySet().forEach(i -> System.out.println(i.getKey() + " , " + i.getValue()));
    }

    /**
     * 基础类型, 使用Arrays.asList后
     */
    @Test
    void byte_array_to_list() {
        byte[] bytes = "Hello".getBytes();

        for (int i = 0; i < bytes.length; i++)
            System.out.print((char) bytes[i]);

        //TODO ?? 不是 List<Byte>?
        // 包括Stream.of() 也会有类似问题
        List<byte[]> bytesToList = Arrays.asList(bytes);
        // List<Byte> bytesToList2 = Arrays.asList(bytes);

        // 看来基础类型都有此问题
        int[] ints = new int[]{1, 2, 3};
        List<int[]> ints1 = Arrays.asList(ints);
    }

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
