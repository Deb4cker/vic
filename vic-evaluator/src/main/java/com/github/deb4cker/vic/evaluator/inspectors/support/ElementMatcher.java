package com.github.deb4cker.vic.evaluator.inspectors.support;

import com.github.deb4cker.vic.evaluator.commons.interfaces.ElementStructure;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class ElementMatcher {

    private ElementMatcher() {
    }

    public static <E extends ElementStructure> Optional<E> findExact(E model, List<E> candidates) {
        return candidates.stream().filter(model::equals).findFirst();
    }

    public static <E extends ElementStructure> Optional<E> findByName(
            String name, List<E> candidates, Function<E, String> nameExtractor) {
        return candidates.stream().filter(e -> nameExtractor.apply(e).equals(name)).findFirst();
    }
}
