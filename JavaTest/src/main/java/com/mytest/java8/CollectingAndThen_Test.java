package com.mytest.java8;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"Duplicates", "Convert2MethodRef"})
public class CollectingAndThen_Test {
    @Test
    void test1() {

        /**
         * 总结就是:
         * 1. collectingAndThen 是一种收集器, 可以实现对原收集器进行包装处理
         * 2. * 第一个参数是原收集器
         *    * 第二个参数是Function, 作用是: 对第一个参数的收集器收集的结果 进一步的处理
         */

        Collectors.collectingAndThen(
                // 1:
                //  Collectors.toList(),
                // 2:
                //  Collectors.toSet(),
                // 3:
                //  Collectors.counting(),
                // 4:
                //  Collectors.summingLong(null),
                // 5:
                Collectors.groupingBy(by -> by.toString()),

                v -> {
                    // 第二个参数是Function
                    // 那 Function.apply()方法的参数 v 是什么?
                    //   v 是 第一个参数(收集器)收集的结果!
                    // 那 Function.apply()方法的返回值 是什么?
                    //   就是对收集器的处理(或者说转换),
                    //   apply()返回值才是collectingAndThen收集器的收集结果
                    //
                    // 1: v 是 list
                    // 2: v 是 set
                    // 3: v 是 long
                    // 4: v 是 long
                    // 5: v 是 map
                    // Set<Map.Entry<Object, List<Object>>> entries = v.entrySet();
                    return null;
                }
        );

        /**
         * 和`groupingBy`联合使用
         */
        Collectors.groupingBy(
                // 第一个是Function, apply()返回值是`groupingBy`结果的key
                (a) -> {
                    return "key";
                },
                // 按key分组后, 每组都对应一个数组元素的list
                // groupingBy(Function) <==> groupingBy(Function, Collectors.toList())
                // 第二个参数就是`对每个list的收集器`
                Collectors.collectingAndThen(
                        // 原收集器, 这里感觉一般都是`toList`
                        Collectors.toList(),
                        // 收集器的处理
                        eachSubList -> {
                            int size = eachSubList.size();
                            // 这里实现的效果是: select key, count(1) / 5
                            return size / 5;
                        }
                )
        );
    }
}
