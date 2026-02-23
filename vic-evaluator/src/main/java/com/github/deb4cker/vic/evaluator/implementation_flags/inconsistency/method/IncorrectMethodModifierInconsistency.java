package com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.method;

import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.ImplementationInconsistency;

import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.INCORRECT_METHOD_MODIFIER;

public class IncorrectMethodModifierInconsistency extends ImplementationInconsistency {
	public IncorrectMethodModifierInconsistency(String methodName, String expectedModifier) {
		super(INCORRECT_METHOD_MODIFIER, methodName, expectedModifier);
	}
}
