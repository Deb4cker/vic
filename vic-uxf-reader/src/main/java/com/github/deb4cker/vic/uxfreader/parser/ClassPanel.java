package com.github.deb4cker.vic.uxfreader.parser;

import com.github.deb4cker.vic.uxfreader.diagram.Coordinates;
import com.github.deb4cker.vic.uxfreader.exception.DiagramSyntaxErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClassPanel {
    private final String[] sections;
    private final String[] headerSectionLines;
    private final String[] attributeSectionLines;
    private final String[] methodSectionLines;
    private final boolean isAbstract;
    private final String className;
    private final Coordinates coordinates;

    private final List<Relation> sources = new ArrayList<>();
    private final List<Relation> targets = new ArrayList<>();

    public ClassPanel(String panelAttributes, Coordinates coordinates) {
        sections = panelAttributes.split("--");

        boolean incorrectNumberOfSections = sections.length != 3;
        DiagramSyntaxErrorException.throwIf(incorrectNumberOfSections);

        headerSectionLines    = splitLines(sections[0]);
        attributeSectionLines = splitLines(sections[1]);
        methodSectionLines    = splitLines(sections[2]);
        isAbstract = headerSectionLines.length > 1;
        className  = headerSectionLines[isAbstract ? 1 : 0];
        this.coordinates = coordinates;
    }

    public String getClassName() {
        return className;
    }

    public String[] getSections() {
        return sections;
    }

    public String[] getHeaderSectionLines() {
        return headerSectionLines;
    }

    public String[] getAttributeSectionLines() {
        return attributeSectionLines;
    }

    public String[] getMethodSectionLines() {
        return methodSectionLines;
    }
    public boolean isAbstract() {
        return isAbstract;
    }

    public void addSource(Relation relation) {
        sources.add(relation);
    }

    public void addTarget(Relation relation) {
        targets.add(relation);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public List<Relation> getSources() {
        return sources;
    }

    public List<Relation> getTargets() {
        return targets;
    }

    private static String[] splitLines(String text){
        return text.split("\n");
    }

    @Override
    public String toString() {
        return className;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClassPanel that = (ClassPanel) o;
        return isAbstract == that.isAbstract && Objects.deepEquals(sections, that.sections) && Objects.deepEquals(headerSectionLines, that.headerSectionLines) && Objects.deepEquals(attributeSectionLines, that.attributeSectionLines) && Objects.deepEquals(methodSectionLines, that.methodSectionLines) && Objects.equals(className, that.className) && Objects.equals(sources, that.sources) && Objects.equals(targets, that.targets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(sections), Arrays.hashCode(headerSectionLines), Arrays.hashCode(attributeSectionLines), Arrays.hashCode(methodSectionLines), isAbstract, className, sources, targets);
    }
}
