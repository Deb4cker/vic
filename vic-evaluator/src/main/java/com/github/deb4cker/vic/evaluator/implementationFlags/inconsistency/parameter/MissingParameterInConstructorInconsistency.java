package com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.parameter;

import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;
import static com.github.deb4cker.vic.evaluator.constants.InconsistencyMessages.MISSING_PARAMETER_IN_CONSTRUCTOR;

public class MissingParameterInConstructorInconsistency extends ImplementationInconsistency {
    public MissingParameterInConstructorInconsistency(String parameterName, String methodName) {
        super(MISSING_PARAMETER_IN_CONSTRUCTOR, parameterName, methodName);
    }
}
