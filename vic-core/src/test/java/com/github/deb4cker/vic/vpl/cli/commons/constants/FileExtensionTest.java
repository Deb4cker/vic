package com.github.deb4cker.vic.vpl.cli.commons.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FileExtension")
class FileExtensionTest {

    @Test
    @DisplayName("JAVA constante tem o valor '.java'")
    void javaConstantValue() {
        assertEquals(".java", FileExtension.JAVA);
    }

    @Test
    @DisplayName("UXF constante tem o valor '.uxf'")
    void uxfConstantValue() {
        assertEquals(".uxf", FileExtension.UXF);
    }
}
