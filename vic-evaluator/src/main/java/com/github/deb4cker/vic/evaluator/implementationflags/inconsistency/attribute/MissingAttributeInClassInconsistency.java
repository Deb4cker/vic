package com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.attribute;

import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.ImplementationInconsistency;

import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.MISSING_ATTRIBUTE_IN_CLASS;

public class MissingAttributeInClassInconsistency extends ImplementationInconsistency {
    public MissingAttributeInClassInconsistency(String fieldName, String className) {
        super(MISSING_ATTRIBUTE_IN_CLASS, fieldName, className);
    }
}
