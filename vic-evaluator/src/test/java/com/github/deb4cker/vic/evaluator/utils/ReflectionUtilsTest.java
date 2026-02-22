package com.github.deb4cker.vic.evaluator.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReflectionUtils")
class ReflectionUtilsTest {

    @SuppressWarnings("unused")
    static class ClassWithOnlyDefaultConstructor {
    }

    @SuppressWarnings("unused")
    static class ClassWithParametrizedConstructorOnly {
        public ClassWithParametrizedConstructorOnly(String name) {
            // intentionally empty — fixture class used only for reflection inspection
        }
    }

    @SuppressWarnings("unused")
    static class ClassWithBothConstructors {
        public ClassWithBothConstructors() {
            // intentionally empty — fixture class used only for reflection inspection
        }

        public ClassWithBothConstructors(String name, int age) {
            // intentionally empty — fixture class used only for reflection inspection
        }
    }

    @SuppressWarnings("unused")
    static class ClassWithMultipleParametrizedConstructors {
        public ClassWithMultipleParametrizedConstructors(String s) {
            // intentionally empty — fixture class used only for reflection inspection
        }

        public ClassWithMultipleParametrizedConstructors(String s, int i) {
            // intentionally empty — fixture class used only for reflection inspection
        }
    }

    @Nested
    @DisplayName("getDeclaredConstructorsExcludingDefault")
    class GetDeclaredConstructorsExcludingDefault {

        @Test
        @DisplayName("retorna array vazio para classe só com construtor padrão")
        void emptyWhenOnlyDefaultConstructor() {
            Constructor<?>[] result = ReflectionUtils
                    .getDeclaredConstructorsExcludingDefault(ClassWithOnlyDefaultConstructor.class);
            assertEquals(0, result.length,
                    "Classe sem construtores explícitos não deveria ter construtores no resultado");
        }

        @Test
        @DisplayName("retorna o construtor parametrizado quando não há construtor padrão")
        void returnsParametrizedConstructor() {
            Constructor<?>[] result = ReflectionUtils
                    .getDeclaredConstructorsExcludingDefault(ClassWithParametrizedConstructorOnly.class);
            assertEquals(1, result.length);
            assertEquals(1, result[0].getParameterCount());
        }

        @Test
        @DisplayName("exclui o construtor padrão mesmo quando há outros construtores")
        void excludesDefaultWhenMixed() {
            Constructor<?>[] result = ReflectionUtils
                    .getDeclaredConstructorsExcludingDefault(ClassWithBothConstructors.class);
            assertEquals(1, result.length);
            assertEquals(2, result[0].getParameterCount());
        }

        @Test
        @DisplayName("retorna todos os construtores parametrizados quando não há padrão")
        void returnsAllParametrizedConstructors() {
            Constructor<?>[] result = ReflectionUtils
                    .getDeclaredConstructorsExcludingDefault(ClassWithMultipleParametrizedConstructors.class);
            assertEquals(2, result.length,
                    "Deveriam ser retornados os 2 construtores parametrizados");
        }
    }
}
