package com.github.deb4cker.vic.evaluator.utils;

import java.lang.reflect.Constructor;
import java.util.Arrays;

@SuppressWarnings("java:S1452")
public final class ReflectionUtils {
    public static Constructor<?>[] getDeclaredConstructorsExcludingDefault(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() > 0)
                .toArray(Constructor[]::new);
    }

    private ReflectionUtils() {
    }
}
