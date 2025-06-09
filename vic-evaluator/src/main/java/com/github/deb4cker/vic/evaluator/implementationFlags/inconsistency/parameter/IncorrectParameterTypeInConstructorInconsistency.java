package com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.parameter;

import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;
import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.INCORRECT_PARAMETER_TYPE_IN_CONSTRUCTOR;

public class IncorrectParameterTypeInConstructorInconsistency extends ImplementationInconsistency {
    public IncorrectParameterTypeInConstructorInconsistency(String parameterName, String methodName) {
        super(INCORRECT_PARAMETER_TYPE_IN_CONSTRUCTOR, parameterName, methodName);
    }
}
