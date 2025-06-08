package com.github.deb4cker.vic.commons.interfaces;

public interface Loggable
{
    default void println(Object... strings) {
        StringBuilder sb = new StringBuilder();
        for (Object string : strings) {
            sb.append(string.toString());
        }

        System.out.println(sb);
    }

    default void print(Object... strings) {
        StringBuilder sb = new StringBuilder();
        for (Object string : strings) {
            sb.append(string.toString());
        }

        System.out.print(sb);
    }

    default void printf(String format, Object... strings) {
        StringBuilder sb = new StringBuilder();
        for (Object string : strings) {
            sb.append(string);
        }

        System.out.printf(format, sb);
    }
}
