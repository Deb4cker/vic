package com.github.deb4cker.vic.evaluator.enums;

public enum AnalysisScope {
    CLASS("Classes"),
    RELATIONSHIP("Relacionamentos");

    private final String value;

    AnalysisScope(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
