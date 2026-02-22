package com.github.deb4cker.vic.vpl.cli.pipeline;

import com.github.deb4cker.vic.commons.StringUtils;
import com.github.deb4cker.vic.evaluator.models.ClassData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class SubmissionVerifier {

    private SubmissionVerifier() {
    }

    public record VerificationResult(List<File> validSubmittedFiles, String message) {
    }

    public static VerificationResult verify(List<ClassData> modeledClasses, List<File> submittedJavaFiles) {
        Map<ClassData, File> respectiveSubmittedClasses = modeledClasses.stream()
                .flatMap(classData -> submittedJavaFiles.stream()
                        .filter(file -> {
                            String submittedName = StringUtils.nameWithoutExtension(file.getName());
                            return submittedName.equals(classData.getClassSimpleName());
                        })
                        .map(file -> Map.entry(classData, file)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<String> notMapped = new ArrayList<>();
        for (ClassData classData : modeledClasses) {
            if (!respectiveSubmittedClasses.containsKey(classData))
                notMapped.add(classData.getClassName());
        }

        StringBuilder builder = new StringBuilder();
        if (!notMapped.isEmpty()) {
            builder.append("Algumas classes não foram submetidas:\n");
            for (String name : notMapped)
                builder.append("\t").append(name).append("\n");
        }

        return new VerificationResult(new ArrayList<>(respectiveSubmittedClasses.values()), builder.toString());
    }
}
