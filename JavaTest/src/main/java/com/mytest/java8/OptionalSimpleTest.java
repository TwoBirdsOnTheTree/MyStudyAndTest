package com.mytest.java8;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OptionalSimpleTest {
    @Test
    void just_test() {
        class Std {
            class Score {
                String subject;
                BigDecimal value;
            }

            Score score;
        }

        Std std = null;
        BigDecimal stdScoreValue = Optional.ofNullable(std)
                .map(m -> m.score)
                .map(m -> m.value)
                .orElse(null);

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
