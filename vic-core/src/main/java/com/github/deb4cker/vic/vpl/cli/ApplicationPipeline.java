package com.github.deb4cker.vic.vpl.cli;

import com.github.deb4cker.vic.evaluator.analyzers.AnalysisResult;
import com.github.deb4cker.vic.evaluator.models.ClassData;
import com.github.deb4cker.vic.uxfreader.exception.SubmittedFileWithCompilationErrorsException;
import com.github.deb4cker.vic.uxfreader.models.ParsedDiagram;
import com.github.deb4cker.vic.vpl.cli.pipeline.*;

import java.io.File;
import java.util.List;

public class ApplicationPipeline {

    private final File uxfFile;
    private final List<File> submittedJavaFiles;
    private final JavaCompilationService compilationService;

    public ApplicationPipeline(File uxfFile, List<File> submittedJavaFiles) {
        this.uxfFile = uxfFile;
        this.submittedJavaFiles = submittedJavaFiles;
        this.compilationService = new JavaCompilationService();
    }

    public String run() {
        try {
            if (uxfFile == null)
                return "Nenhum arquivo UXF encontrado!";

            if (submittedJavaFiles.isEmpty())
                return "Nenhum arquivo Java enviado!";

            ParsedDiagram parsedDiagram = DiagramLoader.load(uxfFile);
            if (parsedDiagram == null)
                return "No parsed diagram found";

            compilationService.compileModeled(TempFileWriter.write(parsedDiagram.parsedClasses()));

            List<ClassData> modeledClasses = compilationService.loadModeledClasses();
            if (modeledClasses.isEmpty())
                return "Classes data not loaded.";

            SubmissionVerifier.VerificationResult verification = SubmissionVerifier.verify(modeledClasses,
                    submittedJavaFiles);
            if (!verification.message().isEmpty())
                return verification.message();

            compilationService.compileSubmitted(verification.validSubmittedFiles());
            List<ClassData> submittedClasses = compilationService.loadSubmittedClasses();

            ClassInspectionRunner.InspectionOutcome outcome = ClassInspectionRunner.run(modeledClasses,
                    submittedClasses);

            List<AnalysisResult> results = outcome.results();
            return ReportCreator.create(results, outcome.sectionCount());

        } catch (SubmittedFileWithCompilationErrorsException e) {
            return e.getMessage();
        }
    }
}
