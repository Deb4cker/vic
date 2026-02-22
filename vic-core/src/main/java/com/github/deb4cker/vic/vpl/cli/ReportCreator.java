package com.github.deb4cker.vic.vpl.cli;

import com.github.deb4cker.vic.evaluator.analyzers.AnalysisResult;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;

import java.util.List;
import java.util.Locale;

public final class ReportCreator {

    private static final double GRADE_SCALE = 10.0;

    public static String create(List<AnalysisResult> results, int exerciseSectionCount) {
        results = results.stream().sorted().toList();
        StringBuilder report = new StringBuilder();
        int correctExerciseCount = exerciseSectionCount;
        for (AnalysisResult analysis : results) {

            String analysisType = switch (analysis.analysisScope()){
                case CLASS -> "Análise da classe " + analysis.scopeName();
                case RELATIONSHIP -> "Análise dos " + analysis.scopeName();
            };

            StringBuilder analysisText = new StringBuilder(analysisType + "\n");

            for (ImplementationFlag flag : analysis.flags()){
                if(!ImplementationFlag.isCorrectFlag(flag)) correctExerciseCount--;
                analysisText.append(flag.toString()).append("\n");
                correctExerciseCount = Math.max(correctExerciseCount, 0);
            }

            report.append(analysisText).append("\n");
        }
        String gradeText = calculateGradeText(correctExerciseCount, exerciseSectionCount);
        report.append(gradeText);

        return report.toString();
    }

    private static String calculateGradeText(int correctExerciseCount, int exerciseSectionCount) {
        double exerciseValue = GRADE_SCALE / exerciseSectionCount;
        String gradeText = String.format(Locale.US, "%.2f", exerciseValue * correctExerciseCount);

        boolean validGradeText = !gradeText.isEmpty() && !gradeText.equals("NaN");

        if (validGradeText)
            return "Nota\n"+ gradeText + "\n";

        return "";
    }

    private ReportCreator(){}
}
