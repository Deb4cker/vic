package com.github.deb4cker.vic.uxfreader.models;

import java.util.List;

public record ParsedDiagram(
        List<ParsedClassObject> parsedClasses
) { }
