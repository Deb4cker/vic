package com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.parameter;

import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.ImplementationInconsistency;
import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.MISSING_PARAMETER_IN_METHOD;

public class MissingParameterInMethodInconsistency extends ImplementationInconsistency {
    public MissingParameterInMethodInconsistency(String parameterName, String methodName) {
        super(MISSING_PARAMETER_IN_METHOD, parameterName, methodName);
    }
}
