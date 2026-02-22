package com.github.deb4cker.vic.vpl.cli.pipeline;

import com.github.deb4cker.vic.commons.StringUtils;
import com.github.deb4cker.vic.evaluator.analyzers.AnalysisResult;
import com.github.deb4cker.vic.evaluator.analyzers.ClassAnalyser;
import com.github.deb4cker.vic.evaluator.analyzers.RelationshipsAnalyzer;
import com.github.deb4cker.vic.evaluator.enums.AnalysisScope;
import com.github.deb4cker.vic.evaluator.models.ClassData;
import com.github.deb4cker.vic.evaluator.models.relations.RelationshipData;
import com.github.deb4cker.vic.uxfreader.exception.ApplicationException;
import com.github.deb4cker.vic.uxfreader.parser.RelationshipMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class ClassInspectionRunner {

    private ClassInspectionRunner() {
    }

    public record InspectionOutcome(List<AnalysisResult> results, int sectionCount) {
    }

    public static InspectionOutcome run(List<ClassData> modeledClasses, List<ClassData> submittedClasses) {
        Map<ClassData, ClassData> pairs = modeledClasses.stream()
                .flatMap(model -> submittedClasses.stream()
                        .filter(sub -> StringUtils.nameWithoutExtension(sub.getClassName())
                                .equals(StringUtils.nameWithoutExtension(model.getClassName())))
                        .map(sub -> Map.entry(model, sub)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<ClassAnalyser> inspectors = new ArrayList<>();
        int[] sectionCount = { 0 };

        pairs.forEach((model, submitted) -> {
            inspectors.add(new ClassAnalyser(model, submitted));
            sectionCount[0] += model.getNumberOfSections();
        });

        List<AnalysisResult> results = runInParallel(inspectors);

        List<RelationshipData> modeledRelations = RelationshipMapper.map(modeledClasses);
        if (!modeledRelations.isEmpty()) {
            List<RelationshipData> submittedRelations = RelationshipMapper.map(submittedClasses);
            RelationshipsAnalyzer analyzer = new RelationshipsAnalyzer(modeledRelations, submittedRelations);
            results.add(new AnalysisResult(AnalysisScope.RELATIONSHIP.getValue(),
                    analyzer.analyze(), AnalysisScope.RELATIONSHIP));
            sectionCount[0] += analyzer.getRelationshipCount();
        }

        return new InspectionOutcome(results, sectionCount[0]);
    }

    private static List<AnalysisResult> runInParallel(List<ClassAnalyser> inspectors) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<AnalysisResult> results = new ArrayList<>();

        try {
            for (ClassAnalyser inspector : inspectors) {
                executor.submit(() -> {
                    try {
                        results.add(new AnalysisResult(inspector.getModelClassDataName(),
                                inspector.inspectClass(), AnalysisScope.CLASS));
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
}
