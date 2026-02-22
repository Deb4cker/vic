package com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.attribute;

import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.ImplementationInconsistency;

import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.INCORRECT_ATTRIBUTE_TYPE;

public class IncorrectAttributeTypeInconsistency extends ImplementationInconsistency {
    public IncorrectAttributeTypeInconsistency(String attributeName, String expectedAttributeType) {
        super(INCORRECT_ATTRIBUTE_TYPE, attributeName, expectedAttributeType);
    }
}
