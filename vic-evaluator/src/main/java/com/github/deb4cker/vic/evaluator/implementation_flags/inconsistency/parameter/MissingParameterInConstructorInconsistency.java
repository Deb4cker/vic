package com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.parameter;

import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.ImplementationInconsistency;
import static com.github.deb4cker.vic.evaluator.commons.constants.InconsistencyMessages.MISSING_PARAMETER_IN_CONSTRUCTOR;

public class MissingParameterInConstructorInconsistency extends ImplementationInconsistency {
    public MissingParameterInConstructorInconsistency(String parameterName, String methodName) {
        super(MISSING_PARAMETER_IN_CONSTRUCTOR, parameterName, methodName);
    }
}
