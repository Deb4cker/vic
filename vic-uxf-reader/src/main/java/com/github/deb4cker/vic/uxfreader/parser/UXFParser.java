package com.github.deb4cker.vic.uxfreader.parser;

import com.github.deb4cker.vic.uxfreader.diagram.*;
import com.github.deb4cker.vic.uxfreader.models.ParsedClassObject;
import com.github.deb4cker.vic.uxfreader.models.ParsedDiagram;
import com.github.deb4cker.vic.commons.interfaces.Loggable;
import com.github.deb4cker.vic.uxfreader.parser.uxfparser.ClassCodeGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UXFParser implements Loggable {

    private final Map<String, ClassPanel> classes = new HashMap<>();

    public ParsedDiagram parseDiagram(Diagram diagram) {
        resolveClassPanels(diagram.getClassElements());
        loadConnections(diagram);

        List<ParsedClassObject> classesOnDiagram = classes.values().stream()
                .map(panel -> ClassCodeGenerator.generate(panel, classes))
                .toList();

        return new ParsedDiagram(classesOnDiagram);
    }

    private void resolveClassPanels(List<ClassElement> classElements) {
        classElements.forEach(classElement -> {
            ClassPanel panel = new ClassPanel(classElement.getPanel_attributes(), classElement.getCoordinates());
            classes.put(panel.getClassName(), panel);
        });
    }

    private void loadConnections(Diagram diagram) {
        for (RelationElement relationElement : diagram.getRelationElements()) {
            RelationCoordinates relationCoordinates = new RelationCoordinates(relationElement);
            RelationPanel relationPanel = new RelationPanel(relationElement.getPanel_attributes(), relationCoordinates);
            Point start = relationCoordinates.getStartPoint();
            Point end = relationCoordinates.getEndPoint();

            ClassPanel source = null;
            ClassPanel target = null;

            for (ClassPanel panel : classes.values()) {
                if (isInside(panel.getCoordinates(), (int) start.x(), (int) start.y()))
                    source = panel;
                if (isInside(panel.getCoordinates(), (int) end.x(), (int) end.y()))
                    target = panel;
            }

            new RelationConnection(source, target, relationPanel).loadRelationInPanels();
        }
    }

    private boolean isInside(Coordinates coords, int x, int y) {
        return x >= coords.getX() && x <= (coords.getX() + coords.getW())
                && y >= coords.getY() && y <= (coords.getY() + coords.getH());
    }
}
