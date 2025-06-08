package com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.method;

import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;
import static com.github.deb4cker.vic.evaluator.constants.InconsistencyMessages.INCORRECT_METHOD_RETURN_TYPE;

public class IncorrectMethodReturnTypeInconsistency extends ImplementationInconsistency {

	public IncorrectMethodReturnTypeInconsistency(String methodName, String expectedReturnType) {
		super(INCORRECT_METHOD_RETURN_TYPE, methodName, expectedReturnType);
	}

}
