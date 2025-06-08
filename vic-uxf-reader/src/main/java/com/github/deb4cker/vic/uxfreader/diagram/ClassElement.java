package com.github.deb4cker.vic.uxfreader.diagram;

public class ClassElement extends Element{
    public ClassElement(Element element) {
        super(element.id, element.panel_attributes, element.coordinates);
    }
}
