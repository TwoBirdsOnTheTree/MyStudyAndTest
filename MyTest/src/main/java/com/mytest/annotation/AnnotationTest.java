package com.mytest.annotation;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AnnotationTest {

    @Test
    @TestAnnotation(test = "ss")
        // @TestInheritAnnotation
    void test5() {
        // 注解的实例化...
        TestAnnotation annotation = new TestAnnotation() {

            @Override
            public String test() {
                return null;
            }

            @Override
            public String test2() {
                return null;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
    }

    @interface OtherAnnotation {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @OtherAnnotation
    @interface TestAnnotation {
        String test();

        String test2() default "d";
    }

    // 不可继承Annotation作为定义注解
    interface TestInheritAnnotation extends Annotation {

        String test();

        @Override
        Class<? extends Annotation> annotationType();
    }
}
