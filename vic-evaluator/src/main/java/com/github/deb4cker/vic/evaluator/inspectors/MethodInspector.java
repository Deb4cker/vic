package com.github.deb4cker.vic.evaluator.inspectors;

import com.github.deb4cker.vic.evaluator.commons.interfaces.ParametrizedElementInspector;
import com.github.deb4cker.vic.evaluator.implementationflags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationflags.correctImplementation.CorrectlyImplementedMethod;
import com.github.deb4cker.vic.evaluator.implementationflags.factory.impl.MethodFlagFactory;
import com.github.deb4cker.vic.evaluator.implementationflags.factory.impl.MethodParameterFlagFactory;
import com.github.deb4cker.vic.evaluator.inspectors.abstracts.AbstractInspector;
import com.github.deb4cker.vic.evaluator.inspectors.structures.MethodStructure;
import com.github.deb4cker.vic.evaluator.inspectors.structures.ParameterStructure;
import com.github.deb4cker.vic.evaluator.utils.InspectorRunner;
import com.github.deb4cker.vic.evaluator.utils.ReverseParser;
import com.github.deb4cker.vic.commons.interfaces.Loggable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.deb4cker.vic.evaluator.commons.constants.SemanticHelpers.OVERLOADS;
import static com.github.deb4cker.vic.evaluator.commons.constants.SemanticHelpers.SINGLE_IMPLEMENTATIONS;

public class MethodInspector extends AbstractInspector<Method, MethodStructure>
        implements ParametrizedElementInspector, Loggable {

    public MethodInspector(Method[] modeledMethods, Method[] submittedMethods, String className) {
        super(new MethodFlagFactory(), modeledMethods, submittedMethods, className);
    }

    @Override
    protected List<ImplementationFlag> getInspectionResult() throws InterruptedException {
        List<ImplementationFlag> flags = new ArrayList<>();

        var computed = trimOverloads(modeledElements);
        var computedSubmitted = trimOverloads(submittedElements);
        List<MethodStructure> modeledMethods = computed.get(SINGLE_IMPLEMENTATIONS);
        List<MethodStructure> submittedMethods = computedSubmitted.get(SINGLE_IMPLEMENTATIONS);

        for (MethodStructure modeledElement : modeledMethods) {
            inspectSingleMethod(modeledElement, submittedMethods, flags);
        }

        flags.addAll(checkOverloads(computed.get(OVERLOADS), computedSubmitted.get(OVERLOADS)));

        return flags;
    }

    private void inspectSingleMethod(MethodStructure modeledElement, List<MethodStructure> submittedMethods,
            List<ImplementationFlag> flags) throws InterruptedException {
        Optional<MethodStructure> match = submittedMethods.stream()
                .filter(s -> s.name().equals(modeledElement.name()))
                .findFirst();

        if (match.isEmpty()) {
            flags.add(flagFactory.createMissingInScopeFlag(modeledElement.name(), scopeName));
            return;
        }

        MethodStructure submittedElement = match.get();
        ParametersInspector parametersInspector = new ParametersInspector(modeledElement.name(),
                new MethodParameterFlagFactory());

        List<ParameterStructure> modeledParameters = parametersInspector.allToStructure(modeledElement.parameters());
        List<ParameterStructure> submittedParameters = parametersInspector
                .allToStructure(submittedElement.parameters());

        parametersInspector.setModeledElements(modeledParameters);
        parametersInspector.setSubmittedElements(submittedParameters);

        InspectorRunner parameterInspection = new InspectorRunner(parametersInspector);
        parameterInspection.start();
        parameterInspection.join();

        List<ImplementationFlag> parameterFlags = new ArrayList<>(parameterInspection.getFlags());
        flags.addAll(parameterFlags);

        boolean correctModifiers = modeledElement.modifier().equals(submittedElement.modifier());
        boolean correctParameters = !ImplementationFlag.hasInconsistencyFlagsIn(parameterFlags);
        boolean correctReturnType = modeledElement.returnType().equals(submittedElement.returnType());

        if (correctModifiers && correctParameters && correctReturnType) {
            flags.add(new CorrectlyImplementedMethod(submittedElement.name()));
            return;
        }

        if (!correctModifiers)
            flags.add(flagFactory.createIncorrectModifierFlag(modeledElement.name(), modeledElement.modifier()));
        if (!correctReturnType)
            flags.add(flagFactory.createIncorrectTypeFlag(modeledElement.name(), modeledElement.returnType()));
    }

    @Override
    protected MethodStructure toStructure(Method element) {
        String modifier = Modifier.toString(element.getModifiers());
        String name = element.getName();
        String returnType = element.getReturnType().toString();
        Parameter[] parameters = element.getParameters();
        return new MethodStructure(modifier, name, returnType, parameters);
    }

    @Override
    protected List<ImplementationFlag> checkOverloads(List<MethodStructure> modeledElements,
            List<MethodStructure> submittedElements)
            throws InterruptedException {
        List<ImplementationFlag> flags = new ArrayList<>();
        for (MethodStructure modelMethod : modeledElements) {
            inspectOverload(modelMethod, submittedElements, flags);
        }
        return flags;
    }

    private void inspectOverload(MethodStructure modelMethod, List<MethodStructure> submittedElements,
            List<ImplementationFlag> flags) throws InterruptedException {
        Optional<MethodStructure> match = submittedElements.stream()
                .filter(s -> methodHasSameSignature(modelMethod, s))
                .filter(s -> s.returnType().equals(modelMethod.returnType()))
                .findFirst();

        String signature = ReverseParser.completeMethodSignatureOf(modelMethod);
        if (match.isEmpty()) {
            flags.add(flagFactory.createMissingInScopeFlag(signature, scopeName));
            return;
        }

        ParametersInspector inspector = new ParametersInspector(
                new MethodParameterFlagFactory(),
                modelMethod.parameters(),
                match.get().parameters(),
                signature);

        InspectorRunner inspection = new InspectorRunner(inspector);
        inspection.start();
        inspection.join();

        flags.addAll(inspection.getFlags());
    }
}
