package com.mytest.annotation;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Annotation_Test {

    @Test
    @AnnotationTest(test = "ss")
        // @TestInheritAnnotation
    void test5() {
        // 注解的实例化...
        AnnotationTest annotation = new AnnotationTest() {

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
                return AnnotationTest.class;
            }
        };
    }

    @interface OtherAnnotation {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @OtherAnnotation
    @interface AnnotationTest {
        String test();

        String test2() default "d";
    }

    // 不可继承Annotation作为定义注解
    interface InheritAnnotationTest extends Annotation {

        String test();

        @Override
        Class<? extends Annotation> annotationType();
    }
}
