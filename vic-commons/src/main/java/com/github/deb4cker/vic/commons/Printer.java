package com.github.deb4cker.vic.commons;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public final class Printer {
    private static final PrintWriter CONSOLE_WRITER = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

    public static void printDirectlyToConsole(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            CONSOLE_WRITER.println(line);
        }
    }

    private Printer() {}
}
