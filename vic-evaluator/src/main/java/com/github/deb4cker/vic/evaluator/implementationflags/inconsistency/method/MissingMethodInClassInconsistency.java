package com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.method;

import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.ImplementationInconsistency;
import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.MISSING_METHOD_IN_CLASS;

public class MissingMethodInClassInconsistency extends ImplementationInconsistency {
    public MissingMethodInClassInconsistency(String methodName, String className) {
        super(MISSING_METHOD_IN_CLASS, methodName, className);
    }
}
