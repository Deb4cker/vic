package com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.parameter;

import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.ImplementationInconsistency;

import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.INCORRECT_PARAMETER_TYPE_IN_METHOD;

public class IncorrectParameterTypeInMethodInconsistency extends ImplementationInconsistency {
    public IncorrectParameterTypeInMethodInconsistency(String parameterName, String methodName, String expectedType) {
        super(INCORRECT_PARAMETER_TYPE_IN_METHOD, parameterName, methodName, expectedType);
    }
}
