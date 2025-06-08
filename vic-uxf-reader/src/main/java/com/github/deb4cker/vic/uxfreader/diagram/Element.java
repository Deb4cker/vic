package com.github.deb4cker.vic.uxfreader.diagram;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "element")
public class Element implements UxfTag
{
    protected String id;
    protected String panel_attributes;
    protected String additional_attributes;
    protected Coordinates coordinates;

    public Element() {
    }

    public Element(String id, String panel_attributes, Coordinates coordinates) {
        this.id = id;
        this.panel_attributes = panel_attributes;
        this.coordinates = coordinates;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "panel_attributes")
    public String getPanel_attributes() {
        return panel_attributes;
    }

    public void setPanel_attributes(String panel_attributes) {
        this.panel_attributes = panel_attributes;
    }

    @XmlElement
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @XmlElement(name = "additional_attributes")
    public String getAdditional_attributes() {
        return additional_attributes;
    }

    public void setAdditional_attributes(String additional_attributes) {
        this.additional_attributes = additional_attributes;
    }

    @Override
    public String getContent() {
        return getPanel_attributes();
    }

    @Override
    public String toString() {
        return getContent();
    }
}
