package com.github.deb4cker.vic.vpl.cli;

import com.github.deb4cker.vic.uxfreader.commons.interfaces.RuntimeCompiler;
import com.github.deb4cker.vic.uxfreader.compilation.InMemoryRuntimeCompiler;
import com.github.deb4cker.vic.uxfreader.diagram.Diagram;
import com.github.deb4cker.vic.uxfreader.diagram.DiagramReader;
import com.github.deb4cker.vic.uxfreader.exception.ApplicationException;
import com.github.deb4cker.vic.uxfreader.exception.SubmittedFileWithCompilationErrorsException;
import com.github.deb4cker.vic.uxfreader.models.ParsedClassObject;
import com.github.deb4cker.vic.uxfreader.models.ParsedDiagram;
import com.github.deb4cker.vic.uxfreader.parser.RelationshipMapper;
import com.github.deb4cker.vic.uxfreader.parser.UMLParser;
import com.github.deb4cker.vic.evaluator.analyzers.AnalysisResult;
import com.github.deb4cker.vic.evaluator.analyzers.ClassAnalyser;
import com.github.deb4cker.vic.evaluator.analyzers.RelationshipsAnalyzer;
import com.github.deb4cker.vic.evaluator.enums.AnalysisScope;
import com.github.deb4cker.vic.evaluator.models.ClassData;
import com.github.deb4cker.vic.evaluator.models.relations.RelationshipData;
import com.github.deb4cker.vic.commons.StringUtils;
import com.github.deb4cker.vic.commons.interfaces.Loggable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ApplicationPipeline implements Loggable {
    private final RuntimeCompiler runtimeCompiler;
    private int exerciseSectionCount = 0;
    private final File uxfFile;
    private final List<File> submittedJavaFiles;

    public ApplicationPipeline(File uxfFile, List<File> submittedJavaFiles) {
        runtimeCompiler = new InMemoryRuntimeCompiler();
        this.uxfFile = uxfFile;
        this.submittedJavaFiles = submittedJavaFiles;
    }

    public String run() {
        try {
            if (uxfFile == null) {
                return "Nenhum arquivo UXF encontrado!";
            }

            if (submittedJavaFiles.isEmpty()) {
                return "Nenhum arquivo Java enviado!";
            }

            ParsedDiagram parsedDiagram = parseDiagram(uxfFile);
            if (parsedDiagram == null) {
                return "No parsed diagram found";
            }

            List<ParsedClassObject> parsedClassObjects = parsedDiagram.parsedClasses();

            List<File> tempJavaFiles = createTempFiles(parsedClassObjects);

            compileModeledJavaFiles(tempJavaFiles);

            List<ClassData> modeledClasses = loadModeledClasses();
            if (modeledClasses.isEmpty()) {
                return "Classes data not loaded.";

            }

            VerificationResult allNecessaryFilesSubmitted = verifySubmittedFiles(modeledClasses, submittedJavaFiles);
            if (!allNecessaryFilesSubmitted.message.isEmpty())
                return allNecessaryFilesSubmitted.message;

            compileSubmittedJavaFiles(allNecessaryFilesSubmitted.validSubmittedFiles());
            List<ClassData> submittedDotClassFiles = loadSubmittedClasses();

            List<RelationshipData> modeledRelations = RelationshipMapper.map(modeledClasses);
            List<RelationshipData> submittedRelations = RelationshipMapper.map(submittedDotClassFiles);

            List<ClassAnalyser> inspectors = createClassInspectors(modeledClasses, submittedDotClassFiles);
            List<AnalysisResult> results = runClassInspectors(inspectors);

            if (!modeledRelations.isEmpty()) {
                RelationshipsAnalyzer relationshipsAnalyzer = new RelationshipsAnalyzer(modeledRelations,
                        submittedRelations);
                AnalysisResult relationshipResults = new AnalysisResult(AnalysisScope.RELATIONSHIP.getValue(),
                        relationshipsAnalyzer.analyze(), AnalysisScope.RELATIONSHIP);
                results.add(relationshipResults);
                exerciseSectionCount += relationshipsAnalyzer.getRelationshipCount();
            }

            return ReportCreator.create(results, exerciseSectionCount);
        } catch (SubmittedFileWithCompilationErrorsException e) {
            return e.getMessage();
        }
    }

    private void compileModeledJavaFiles(List<File> tempJavaFiles) {
        runtimeCompiler.compileJavaFiles(tempJavaFiles, true);
    }

    public List<File> createTempFiles(List<ParsedClassObject> parsedFiles) {
        List<File> tempFiles = new ArrayList<>();

        try {
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));

            for (ParsedClassObject parsedFile : parsedFiles) {
                File tempFile = new File(tempDir.toFile(), parsedFile.className() + ".java");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                    writer.write(parsedFile.classText());
                }
                tempFiles.add(tempFile);
            }
        } catch (IOException e) {
            throw new ApplicationException("Error creating temp file: " + e.getMessage());
        }

        return tempFiles;
    }

    private List<ClassAnalyser> createClassInspectors(List<ClassData> modeledClasses,
            List<ClassData> submittedDotClassFiles) {
        Map<ClassData, ClassData> modelAndSubmissionPairs = modeledClasses.stream()
                .flatMap(model -> submittedDotClassFiles.stream()
                        .filter(submittedClass -> StringUtils.nameWithoutExtension(submittedClass.getClassName())
                                .equals(StringUtils.nameWithoutExtension(model.getClassName())))
                        .map(submittedClass -> Map.entry(model, submittedClass)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<ClassAnalyser> inspections = new ArrayList<>();

        modelAndSubmissionPairs.entrySet().forEach(entry -> {
            inspections.add(new ClassAnalyser(entry.getKey(), entry.getValue()));
            exerciseSectionCount += entry.getKey().getNumberOfSections();
        });

        return inspections;
    }

    private List<AnalysisResult> runClassInspectors(List<ClassAnalyser> inspectors) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<AnalysisResult> results = new ArrayList<>();

        try {
            for (ClassAnalyser inspector : inspectors) {
                executor.submit(() -> {
                    try {
                        results.add(new AnalysisResult(inspector.getModelClassDataName(), inspector.inspectClass(),
                                AnalysisScope.CLASS));

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new ApplicationException(e.getMessage());
                    }
                });
            }
            executor.shutdown();

            boolean terminated = executor.awaitTermination(1, TimeUnit.MINUTES);
            if (!terminated)
                executor.shutdownNow();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        } finally {
            if (!executor.isTerminated())
                executor.shutdownNow();
        }

        return results;
    }

    private ParsedDiagram parseDiagram(File uxfFile) {
        DiagramReader reader = new DiagramReader(uxfFile);
        Diagram diagram = reader.getDiagram();
        UMLParser parser = new UMLParser();
        return parser.parseDiagram(diagram);
    }

    private void compileSubmittedJavaFiles(List<File> javaFiles) {
        runtimeCompiler.compileJavaFiles(javaFiles, false);
    }

    private List<ClassData> loadModeledClasses() {
        Map<String, Class<?>> compiledClasses = ((InMemoryRuntimeCompiler) runtimeCompiler).getModeledClasses();
        return compiledClasses.values().stream()
                .map(ClassData::new)
                .toList();
    }

    private List<ClassData> loadSubmittedClasses() {
        Map<String, Class<?>> compiledClasses = ((InMemoryRuntimeCompiler) runtimeCompiler).getSubmittedClasses();
        return compiledClasses.values().stream()
                .map(ClassData::new)
                .toList();
    }

    public record VerificationResult(List<File> validSubmittedFiles, String message) {
    }

    public VerificationResult verifySubmittedFiles(List<ClassData> modeledClasses, List<File> submittedJavaFiles) {
        Map<ClassData, File> respectiveSubmittedClasses = modeledClasses.stream()
                .flatMap(classData -> submittedJavaFiles.stream()
                        .filter(file -> {
                            String submittedFileClassName = StringUtils.nameWithoutExtension(file.getName());
                            String modeledClassName = classData.getClassSimpleName();
                            return submittedFileClassName.equals(modeledClassName);
                        })
                        .map(file -> Map.entry(classData, file)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<String> notMappedClasses = new ArrayList<>();

        for (ClassData classData : modeledClasses) {
            if (!respectiveSubmittedClasses.containsKey(classData)) {
                notMappedClasses.add(classData.getClassName());
            }
        }

        StringBuilder builder = new StringBuilder();
        if (!notMappedClasses.isEmpty()) {
            builder.append("Algumas classes não foram submetidas:\n");
            for (String notMappedClass : notMappedClasses) {
                builder.append("\t").append(notMappedClass).append("\n");
            }
        }

        List<File> validFiles = new ArrayList<>(respectiveSubmittedClasses.values());

        return new VerificationResult(validFiles, builder.toString());
    }
}
