package com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.parameter;

import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;
import static com.github.deb4cker.vic.evaluator.constants.InconsistencyMessages.MISSING_PARAMETER_IN_METHOD;

public class MissingParameterInMethodInconsistency extends ImplementationInconsistency {
    public MissingParameterInMethodInconsistency(String parameterName, String methodName) {
        super(MISSING_PARAMETER_IN_METHOD, parameterName, methodName);
    }
}
