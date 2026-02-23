package com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.method;

import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.ImplementationInconsistency;
import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.INCORRECT_METHOD_RETURN_TYPE;

public class IncorrectMethodReturnTypeInconsistency extends ImplementationInconsistency {

	public IncorrectMethodReturnTypeInconsistency(String methodName, String expectedReturnType) {
		super(INCORRECT_METHOD_RETURN_TYPE, methodName, expectedReturnType);
	}

}
