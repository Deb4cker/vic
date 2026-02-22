package com.github.deb4cker.vic.vpl.cli.pipeline;

import com.github.deb4cker.vic.uxfreader.exception.ApplicationException;
import com.github.deb4cker.vic.uxfreader.models.ParsedClassObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class TempFileWriter {

    private TempFileWriter() {
    }

    public static List<File> write(List<ParsedClassObject> parsedClasses) {
        List<File> tempFiles = new ArrayList<>();
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));

        try {
            for (ParsedClassObject parsed : parsedClasses) {
                File tempFile = new File(tempDir.toFile(), parsed.className() + ".java");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                    writer.write(parsed.classText());
                }
                tempFiles.add(tempFile);
            }
        } catch (IOException e) {
            throw new ApplicationException("Error creating temp file: " + e.getMessage());
        }

        return tempFiles;
    }
}
