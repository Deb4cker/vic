package com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.attribute;

import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.ImplementationInconsistency;

import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.INCORRECT_ATTRIBUTE_MODIFIER;

public class IncorrectAttributeModifierInconsistency extends ImplementationInconsistency {
    public IncorrectAttributeModifierInconsistency(String attributeName, String expectedAttributeType) {
        super(INCORRECT_ATTRIBUTE_MODIFIER, attributeName, expectedAttributeType);
    }
}
