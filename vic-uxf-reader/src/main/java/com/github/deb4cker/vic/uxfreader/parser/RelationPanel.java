package com.github.deb4cker.vic.uxfreader.parser;

import java.util.Arrays;

public class RelationPanel {

    private String lt;
    private String m1;
    private String m2;
    private String direction;
    private final RelationCoordinates relationCoordinates;

    public RelationPanel(String panelAttributes, RelationCoordinates relationCoordinates) {
        this.relationCoordinates = relationCoordinates;
        String[] sections = Arrays.stream(panelAttributes.split("\n"))
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);

        findFields(sections);
    }

    public String getLt() {
        return lt;
    }

    public String getM1() {
        return m1;
    }

    public String getM2() {
        return m2;
    }

    public String getDirection() {
        return direction;
    }

    public RelationCoordinates getRelationCoordinates() {
        return relationCoordinates;
    }

    private void findFields(String[] sections) {
        for (String section : sections) {
            parseSection(section);
            if (lt != null && m1 != null && m2 != null)
                break;
        }
    }

    private void parseSection(String section) {
        if (section == null || section.isBlank())
            return;

        String trimmed = section.trim();

        if (trimmed.startsWith("lt=")) {
            lt = trimmed.replace("lt=", "");
            return;
        }
        if (trimmed.startsWith("m1=")) {
            m1 = trimmed.replace("m1=", "");
            return;
        }
        if (trimmed.startsWith("m2=")) {
            m2 = trimmed.replace("m2=", "");
            return;
        }

        direction = section;
    }
}
