package com.github.deb4cker.vic.evaluator.inspectors;

import com.github.deb4cker.vic.commons.EvaluationBypassCode;
import com.github.deb4cker.vic.evaluator.implementationflags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationflags.factory.interfaces.ParameterFlagFactory;
import com.github.deb4cker.vic.evaluator.inspectors.abstracts.AbstractInspector;
import com.github.deb4cker.vic.evaluator.inspectors.structures.ParameterStructure;
import com.github.deb4cker.vic.evaluator.inspectors.support.ElementMatcher;
import com.github.deb4cker.vic.commons.StringUtils;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParametersInspector extends AbstractInspector<Parameter, ParameterStructure> {

    public ParametersInspector(ParameterFlagFactory flagFactory, Parameter[] modeledParameters,
            Parameter[] submittedParameters, String scopeName) {
        super(flagFactory, modeledParameters, submittedParameters, scopeName);
    }

    public ParametersInspector(String scopeName, ParameterFlagFactory flagFactory) {
        super(flagFactory, new Parameter[] {}, new Parameter[] {}, scopeName);
    }

    @Override
    public List<ImplementationFlag> getInspectionResult() {
        List<ImplementationFlag> flags = new ArrayList<>();
        modeledElements.stream()
                .filter(p -> !p.name().contains(EvaluationBypassCode.CODE))
                .forEach(model -> inspectParameter(model, flags));
        return flags;
    }

    private void inspectParameter(ParameterStructure model, List<ImplementationFlag> flags) {
        Optional<ParameterStructure> exact = ElementMatcher.findExact(model, submittedElements);
        if (exact.isPresent())
            return;

        Optional<ParameterStructure> byName = ElementMatcher.findByName(model.name(), submittedElements,
                ParameterStructure::name);
        if (byName.isEmpty()) {
            flags.add(flagFactory.createMissingInScopeFlag(model.name(), scopeName));
            return;
        }

        if (!model.type().equals(byName.get().type()))
            flags.add(flagFactory.createIncorrectTypeFlag(model.name(), scopeName, model.type()));
    }

    @Override
    protected ParameterStructure toStructure(Parameter parameter) {
        String type = StringUtils.nameWithoutExtension(parameter.getType().getName());
        String name = parameter.getName();
        return new ParameterStructure(type, name);
    }
}
