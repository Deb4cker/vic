package com.github.deb4cker.vic.evaluator.analyzers;

import com.github.deb4cker.vic.evaluator.enums.AnalysisScope;
import com.github.deb4cker.vic.evaluator.implementationflags.ImplementationFlag;

import java.util.List;

public record AnalysisResult(
        String scopeName,
        List<ImplementationFlag> flags,
        AnalysisScope analysisScope
) implements Comparable<AnalysisResult> {

    @Override
    public int compareTo(AnalysisResult otherResult) {
        int byScope = this.analysisScope.compareTo(otherResult.analysisScope);
        if (byScope != 0) return byScope;
        return this.scopeName.compareTo(otherResult.scopeName);
    }
}
