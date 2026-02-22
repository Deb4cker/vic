package com.github.deb4cker.vic.uxfreader.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RelationshipMapper — getTypeFromCollection")
class RelationshipMapperTest {

    @SuppressWarnings("unused")
    static class WithList {
        private List<String> nomes;
    }

    @SuppressWarnings("unused")
    static class WithArrayList {
        private ArrayList<Integer> numeros;
    }

    @SuppressWarnings("unused")
    static class WithMap {
        private Map<String, Double> precos;
    }

    @SuppressWarnings("unused")
    static class WithArray {
        private String[] palavras;
    }

    @SuppressWarnings("unused")
    static class WithRawList {
        @SuppressWarnings("rawtypes")
        private List semGenerics;
    }

    @Nested
    @DisplayName("getTypeFromCollection")
    class GetTypeFromCollection {

        private Field field(Class<?> clazz, String name) throws NoSuchFieldException {
            return clazz.getDeclaredField(name);
        }

        @Test
        @DisplayName("List<String> → String.class")
        void listOfStringReturnsStringType() throws NoSuchFieldException {
            Field f = field(WithList.class, "nomes");
            Class<?> type = RelationshipMapper.getTypeFromCollection(f);
            assertEquals(String.class, type);
        }

        @Test
        @DisplayName("ArrayList<Integer> → Integer.class")
        void arrayListOfIntegerReturnsIntegerType() throws NoSuchFieldException {
            Field f = field(WithArrayList.class, "numeros");
            Class<?> type = RelationshipMapper.getTypeFromCollection(f);
            assertEquals(Integer.class, type);
        }

        @Test
        @DisplayName("Map<String,Double> → Double.class (valor do map)")
        void mapReturnsValueType() throws NoSuchFieldException {
            Field f = field(WithMap.class, "precos");
            Class<?> type = RelationshipMapper.getTypeFromCollection(f);
            assertEquals(Double.class, type);
        }

        @Test
        @DisplayName("String[] → String.class (array component type)")
        void arrayReturnsComponentType() throws NoSuchFieldException {
            Field f = field(WithArray.class, "palavras");
            Class<?> type = RelationshipMapper.getTypeFromCollection(f);
            assertEquals(String.class, type);
        }

        @Test
        @DisplayName("List sem type argument retorna null")
        void rawListReturnsNull() throws NoSuchFieldException {
            Field f = field(WithRawList.class, "semGenerics");
            Class<?> type = RelationshipMapper.getTypeFromCollection(f);
            assertNull(type, "List sem generic deveria retornar null");
        }
    }
}
