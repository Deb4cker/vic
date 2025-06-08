package com.github.deb4cker.vic.uxfreader.diagram;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.github.deb4cker.vic.uxfreader.commons.constants.ElementType.RELATION;
import static com.github.deb4cker.vic.uxfreader.commons.constants.ElementType.UML_CLASS;

@XmlRootElement(name = "diagram")
public class Diagram implements UxfTag
{
    @XmlElement(name = "element")
    private final List<Element> elements = new ArrayList<>();

    public List<Element> getElements(){
        return elements;
    }

    @Override
    public String getContent() {
        StringBuilder stringBuilder = new StringBuilder();
        elements.forEach(element -> stringBuilder.append(element.getContent()).append("\n"));
        return stringBuilder.toString();
    }

    public List<ClassElement> getClassElements() {
        return getUmlElementsById(UML_CLASS)
                .map(ClassElement::new)
                .toList();
    }

    public List<RelationElement> getRelationElements() {
        return getUmlElementsById(RELATION)
                .map(RelationElement::new)
                .toList();
    }

    private Stream<Element> getUmlElementsById(String id) {
        return elements.stream()
                .filter(element -> element.getId().equals(id));
    }

    @Override
    public String toString() {
        return getContent();
    }
}
