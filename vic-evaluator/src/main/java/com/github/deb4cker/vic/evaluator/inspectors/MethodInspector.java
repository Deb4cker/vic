package com.github.deb4cker.vic.evaluator.inspectors;

import com.github.deb4cker.vic.evaluator.interfaces.ParametrizedElementInspector;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationFlags.correctImplementation.CorrectlyImplementedMethod;
import com.github.deb4cker.vic.evaluator.implementationFlags.factory.impl.MethodFlagFactory;
import com.github.deb4cker.vic.evaluator.implementationFlags.factory.impl.MethodParameterFlagFactory;
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

import static com.github.deb4cker.vic.evaluator.constants.SemanticHelpers.OVERLOADS;
import static com.github.deb4cker.vic.evaluator.constants.SemanticHelpers.SINGLE_IMPLEMENTATIONS;

public class MethodInspector extends AbstractInspector<Method, MethodStructure> implements ParametrizedElementInspector, Loggable {

    public MethodInspector(Method[] modeledMethods, Method[] submittedMethods, String className) {
        super(new MethodFlagFactory(), modeledMethods, submittedMethods, className);
    }

    @Override
    protected List<ImplementationFlag> getInspectionResult() throws InterruptedException {
        List<ImplementationFlag> flags = new ArrayList<>();

        var computedModeledStructures = trimOverloads(modeledElements);
        var computedSubmittedStructures = trimOverloads(submittedElements);
        List<MethodStructure> modeledMethods = computedModeledStructures.get(SINGLE_IMPLEMENTATIONS);
        List<MethodStructure> submittedMethods = computedSubmittedStructures.get(SINGLE_IMPLEMENTATIONS);

        ParametersInspector parametersInspector;
        InspectorRunner parameterInspection;

        Inspection:
        for (MethodStructure modeledElement : modeledMethods) {

            boolean isImplemented = modeledElements.stream()
                    .anyMatch(modelMethod ->
                            submittedElements.stream().anyMatch(submittedMethod -> submittedMethod.name().equals(modelMethod.name()))
                    );

            boolean correctModifiers = true;
            boolean correctParameters;
            boolean correctReturnType = true;

            parametersInspector = new ParametersInspector(modeledElement.name(), new MethodParameterFlagFactory());

            for (MethodStructure submittedElement : submittedMethods) {

                isImplemented = submittedElement.name().equals(modeledElement.name());
                if (!isImplemented) continue;

                List<ParameterStructure> modeledParameters = parametersInspector.allToStructure(modeledElement.parameters());
                List<ParameterStructure> submittedParameters = parametersInspector.allToStructure(submittedElement.parameters());

                parametersInspector.setModeledElements(modeledParameters);
                parametersInspector.setSubmittedElements(submittedParameters);
                parameterInspection = new InspectorRunner(parametersInspector);

                parameterInspection.start();
                parameterInspection.join();

                List<ImplementationFlag> parameterFlags = new ArrayList<>(parameterInspection.getFlags());
                flags.addAll(parameterFlags);

                correctModifiers = modeledElement.modifier().equals(submittedElement.modifier());
                correctParameters = !ImplementationFlag.hasInconsistencyFlagsIn(parameterFlags);
                correctReturnType = modeledElement.returnType().equals(submittedElement.returnType());

                boolean totallyCorrect = correctModifiers && correctParameters && correctReturnType;
                if (totallyCorrect) {
                    flags.add(new CorrectlyImplementedMethod(submittedElement.name()));
                    continue Inspection;
                }
                break;
            }

            if (!isImplemented) {
                flags.add(flagFactory.createMissingInScopeFlag(modeledElement.name(), scopeName));
            }

            if (!correctModifiers) {
                flags.add(flagFactory.createIncorrectModifierFlag(modeledElement.name(), modeledElement.modifier()));
            }

            if (!correctReturnType) {
                flags.add(flagFactory.createIncorrectTypeFlag(modeledElement.name(), modeledElement.returnType()));
            }
        }

        List<MethodStructure> modeledOverloads = computedModeledStructures.get(OVERLOADS);
        List<MethodStructure> submittedOverloads = computedSubmittedStructures.get(OVERLOADS);
        flags.addAll(checkOverloads(modeledOverloads, submittedOverloads));

        return flags;
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
    protected List<ImplementationFlag> checkOverloads(List<MethodStructure> modeledElements, List<MethodStructure> submittedElements) throws InterruptedException {
        List<ImplementationFlag> flags = new ArrayList<>();
        for (MethodStructure modelMethod : modeledElements) {
            Optional<MethodStructure> matchingMethod = submittedElements.stream()
                    .filter(submittedMethod -> methodHasSameSignature(modelMethod, submittedMethod))
                    .filter(submittedMethod -> submittedMethod.returnType().equals(modelMethod.returnType()))
                    .findFirst();

            String completeMethodSignature = ReverseParser.completeMethodSignatureOf(modelMethod);
            if(matchingMethod.isEmpty()) {
                flags.add(flagFactory.createMissingInScopeFlag(completeMethodSignature, scopeName));
                break;
            }

            ParametersInspector inspector = new ParametersInspector(
                    new MethodParameterFlagFactory(),
                    modelMethod.parameters(),
                    matchingMethod.get().parameters(),
                    completeMethodSignature
            );

            InspectorRunner inspection = new InspectorRunner(inspector);
            inspection.start();
            inspection.join();

            List<ImplementationFlag> parameterFlags = new ArrayList<>(inspection.getFlags());
            flags.addAll(parameterFlags);
        }

        return flags;
    }
}

