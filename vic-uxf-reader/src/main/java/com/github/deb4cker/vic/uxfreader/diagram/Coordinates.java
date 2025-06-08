package com.github.deb4cker.vic.uxfreader.diagram;

import jakarta.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "coordinates")
public class Coordinates
{
    private int x;
    private int y;
    private int w;
    private int h;

    public Coordinates() {
    }

    public Coordinates(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @XmlElement
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @XmlElement
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @XmlElement
    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    @XmlElement
    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
}