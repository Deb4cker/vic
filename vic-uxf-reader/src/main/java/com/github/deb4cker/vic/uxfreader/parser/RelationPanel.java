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
            if (section == null || section.isBlank()) continue;

            if (section.startsWith("lt=")) {
                lt = section.trim().replace("lt=", "");
                continue;
            }

            if (section.startsWith("m1=")) {
                m1 = section.trim().replace("m1=", "");
                continue;
            }

            if (section.startsWith("m2=")) {
                m2 = section.trim().replace("m2=", "");
                continue;
            }

            direction = section;

            if (lt != null && m1 != null && m2 != null) break;
        }
    }
}
