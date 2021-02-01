package com.mytest.java8;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OptionalSimpleTest {
    @Test
    void just_test() {
        class Student {
            class SubjectScore {
                String subject;
                BigDecimal score;
            }

            SubjectScore subjectScore;
            SubjectScore defaultSubjectScore = new SubjectScore() {{
                score = BigDecimal.TEN;
                subject = "语文";
            }};
        }

        Student std = null;
        Student std2 = new Student() {{
            subjectScore = defaultSubjectScore;
        }};

        // std = std2;

        // 模拟解决`多层判断null` if(std == null) if(std.subjectScore == null)
        String stdScoreValue = Optional.ofNullable(std)
                .map(m -> m.subjectScore)
                .map(m -> String.format("科目: %s, 分数: %s", m.subject, m.score))
                .orElse("为空时显示");

        System.out.println("std的score的value是: " + stdScoreValue);
    }

    @Test
    void collection_optional() {
        List<String> list = null;

        isCollectionEmpty(list).ifPresent(System.out::println);

        Optional.empty().ifPresent(c -> System.out.println("Optional.empty()"));
    }

    private <T> Optional<Collection<T>> isCollectionEmpty(Collection<T> collection) {
        if (null == collection || collection.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(collection);
    }
}
