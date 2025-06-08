package com.github.deb4cker.vic.uxfreader.parser;

import com.github.deb4cker.vic.uxfreader.diagram.Element;

public class RelationCoordinates {
    private final Polyline polyline;

    public RelationCoordinates(Element relation) {
        String[] coords = relation.getAdditional_attributes().split(";");
        float x1 = Float.parseFloat(coords[0].trim());
        float y1 = Float.parseFloat(coords[1].trim());
        float x2 = Float.parseFloat(coords[2].trim());
        float y2 = Float.parseFloat(coords[3].trim());

        int baseX = relation.getCoordinates().getX();
        int baseY = relation.getCoordinates().getY();

        Point start = new Point(baseX + x1, baseY + y1);
        Point end = new Point(baseX + x2, baseY + y2);

        this.polyline = new Polyline(start, end);
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public Point getStartPoint() {
        return polyline.start();
    }

    public Point getEndPoint() {
        return polyline.end();
    }
}
