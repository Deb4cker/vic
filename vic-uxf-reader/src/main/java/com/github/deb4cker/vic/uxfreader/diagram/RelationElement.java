package com.github.deb4cker.vic.uxfreader.diagram;

public class RelationElement extends Element {
    public RelationElement(Element element) {
        super(element.id, element.panel_attributes, element.coordinates);
        this.additional_attributes = element.additional_attributes;
    }
}
