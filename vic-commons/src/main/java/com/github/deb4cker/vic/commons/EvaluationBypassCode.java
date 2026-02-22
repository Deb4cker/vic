package com.github.deb4cker.vic.commons;

import java.util.UUID;

public final class EvaluationBypassCode {
    public static final String CODE = UUID.randomUUID()
            .toString()
            .replaceAll("[^a-zA-Z]", "")
            .toLowerCase();

    private static int initValue = 1;

    public static String generateEvaluationBypassCode() {
        return CODE + (++initValue);
    }

    private EvaluationBypassCode() {}
}
