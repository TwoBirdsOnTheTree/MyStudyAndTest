package com.mytest.let_it_be_me;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Java8 的方法参数名 反射
 * fuck 还有加 -parameters, 那有什么用!
 */
public class Java8_Method_Parameter_Name_Reflection {
    private void invokeMethod(String a, Integer b) {
        method();
    }

    private void method() {
        try {
            StackTraceElement[] traces = Thread.currentThread().getStackTrace();
            Stream.of(traces).map(i -> i.getMethodName()).forEach(i -> System.out.print(" , " + i));
            System.out.println();
            System.out.println();

            StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
            String methodName = trace.getMethodName();
            String className = trace.getClassName();

            Class<?> clazz = Class.forName(className);
            Method method = Stream.of(clazz.getDeclaredMethods()).filter(m -> m.getName().equals(methodName))
                    .findAny().orElse(null);
            Objects.requireNonNull(method);
            Stream.of(method.getParameters()).forEach(p -> {
                System.out.println(p.getName());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void entrence() {
        invokeMethod("Hello", 100);
    }
}
