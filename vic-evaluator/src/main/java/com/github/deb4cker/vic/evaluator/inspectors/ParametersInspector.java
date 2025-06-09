package com.github.deb4cker.vic.evaluator.inspectors;

import com.github.deb4cker.vic.commons.EvaluationBypassCode;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationFlags.factory.interfaces.ParameterFlagFactory;
import com.github.deb4cker.vic.evaluator.inspectors.abstracts.AbstractInspector;
import com.github.deb4cker.vic.evaluator.inspectors.structures.ParameterStructure;
import com.github.deb4cker.vic.commons.StringUtils;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParametersInspector extends AbstractInspector<Parameter, ParameterStructure> {

    public ParametersInspector(ParameterFlagFactory flagFactory, Parameter[] modeledParameters, Parameter[] submittedParameters, String scopeName) {
        super(flagFactory, modeledParameters, submittedParameters, scopeName);
    }

    public ParametersInspector(String scopeName, ParameterFlagFactory flagFactory) {
        super(flagFactory, new Parameter[]{}, new Parameter[]{}, scopeName);
    }

    @Override
    public List<ImplementationFlag> getInspectionResult() {

        List<ImplementationFlag> flags = new ArrayList<>();
        for (ParameterStructure modeledParameter : modeledElements) {

            boolean nonEvaluatedElement = modeledParameter.name().contains(EvaluationBypassCode.CODE);
            if(nonEvaluatedElement) continue;

            boolean isImplemented = false;
            boolean correctType = true;

            for (ParameterStructure submittedParameter : submittedElements) {
                boolean totallyCorrect = modeledParameter.equals(submittedParameter);
                if (totallyCorrect) {
                    isImplemented = true;
                    break;
                }

                isImplemented = modeledParameter.name().equals(submittedParameter.name());

                if(isImplemented) {
                    correctType = modeledParameter.type().equals(submittedParameter.type());
                    break;
                }
            }

            if (!isImplemented) {
                flags.add(flagFactory.createMissingInScopeFlag(modeledParameter.name(), scopeName));
            }

            if (!correctType) {
                flags.add(flagFactory.createIncorrectTypeFlag(modeledParameter.name(), scopeName, modeledParameter.type()));
            }
        }

        return flags;
    }

    @Override
    protected ParameterStructure toStructure(Parameter parameter) {
        String type = StringUtils.nameWithoutExtension(parameter.getType().getName());
        String name = parameter.getName();

        return new ParameterStructure(type, name);
    }
}
