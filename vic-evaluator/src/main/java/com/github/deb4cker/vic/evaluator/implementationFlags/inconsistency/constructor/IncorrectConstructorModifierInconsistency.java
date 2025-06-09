package com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.constructor;

import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;

import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.INCORRECT_CONSTRUCTOR_MODIFIER;

public class IncorrectConstructorModifierInconsistency extends ImplementationInconsistency {
    public IncorrectConstructorModifierInconsistency(String className, String expectedModifier) {
        super(INCORRECT_CONSTRUCTOR_MODIFIER, className, expectedModifier);
    }
}
