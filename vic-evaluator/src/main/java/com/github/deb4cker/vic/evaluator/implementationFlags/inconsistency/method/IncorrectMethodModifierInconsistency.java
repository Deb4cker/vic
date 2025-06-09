package com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.method;

import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;

import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.INCORRECT_METHOD_MODIFIER;

public class IncorrectMethodModifierInconsistency extends ImplementationInconsistency {
	public IncorrectMethodModifierInconsistency(String methodName, String expectedModifier) {
		super(INCORRECT_METHOD_MODIFIER, methodName, expectedModifier);
	}
}
