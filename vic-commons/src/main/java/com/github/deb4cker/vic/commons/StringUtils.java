package com.github.deb4cker.vic.commons;

public final class StringUtils {
    public static String nameWithoutExtension(String str) {
        String fileName = str.substring(str.lastIndexOf('/') + 1);
        fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) fileName = fileName.substring(0, lastDotIndex);
        return fileName;
    }

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String onlyClassNameOf(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String pluralize(String word) {
        if (word == null || word.length() < 2) return word;

        word = word.trim();
        String wordLower = word.toLowerCase();
        int len = word.length();

        if (wordLower.endsWith("r") || wordLower.endsWith("s") || wordLower.endsWith("z")) {
            return word + "es";
        }

        if (wordLower.endsWith("m")) {
            return word.substring(0, len - 1) + "ns";
        }

        if (wordLower.endsWith("ão")) {
            return word.substring(0, len - 2) + "ões";
        }

        if (wordLower.endsWith("il")) {
            return word.substring(0, len - 2) + "is";
        }

        if (wordLower.endsWith("l")) {
            return word.substring(0, len - 1) + "is";
        }

        return word + "s";
    }

    private StringUtils(){}
}
