package com.github.deb4cker.vic.commons;

public class StringUtils {
    public static String nameWithoutExtension(String str) {
        String fileName = str.substring(str.lastIndexOf('/') + 1);
        fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) fileName = fileName.substring(0, lastDotIndex);
        return fileName;
    }

    public static String onlyClassNameOf(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String pluralize(String word) {
        if (word == null || word.isEmpty()) return word;

        word = word.trim();

        if (word.endsWith("r") || word.endsWith("s") || word.endsWith("z")) {
            return word + "es";
        }
        if (word.endsWith("m")) {
            return word.substring(0, word.length() - 1) + "ns";
        }

        String substring = word.substring(0, word.length() - 2);

        if (word.endsWith("ão")) {
            return substring + "ões";
        }
        if (word.endsWith("l")) {
            return word.substring(0, word.length() - 1) + "is";
        }
        if (word.endsWith("il")) {
            return substring + "is";
        }
        if (word.endsWith("al") || word.endsWith("el") || word.endsWith("ul") || word.endsWith("ol")) {
            return substring + "eis";
        }

        if (!word.endsWith("s")) {
            return word + "s";
        }

        return word;
    }
}
