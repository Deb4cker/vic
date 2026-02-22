package com.github.deb4cker.vic.vpl.cli.pipeline;

import com.github.deb4cker.vic.uxfreader.diagram.Diagram;
import com.github.deb4cker.vic.uxfreader.diagram.DiagramReader;
import com.github.deb4cker.vic.uxfreader.models.ParsedDiagram;
import com.github.deb4cker.vic.uxfreader.parser.UXFParser;

import java.io.File;

public final class DiagramLoader {

    private DiagramLoader() {
    }

    public static ParsedDiagram load(File uxfFile) {
        DiagramReader reader = new DiagramReader(uxfFile);
        Diagram diagram = reader.getDiagram();
        return new UXFParser().parseDiagram(diagram);
    }
}
