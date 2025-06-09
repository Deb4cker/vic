package com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.constructor;

import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;

import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.MISSING_CONSTRUCTOR_IN_CLASS;

public class MissingConstructorInClassInconsistency extends ImplementationInconsistency {
    public MissingConstructorInClassInconsistency(String className) {
        super(MISSING_CONSTRUCTOR_IN_CLASS, className);
    }
}
