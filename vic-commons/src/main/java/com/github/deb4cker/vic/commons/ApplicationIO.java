package com.github.deb4cker.vic.commons;

import java.io.PrintStream;

import static java.lang.System.*;

public final class ApplicationIO {
    private ApplicationIO() {
    }

    public static final PrintStream STANDARD_OUT = out;
    public static final PrintStream ERROR_OUT = err;
}
