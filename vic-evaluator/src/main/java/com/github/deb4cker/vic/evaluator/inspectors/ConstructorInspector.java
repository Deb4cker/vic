package com.github.deb4cker.vic.evaluator.inspectors;

import com.github.deb4cker.vic.evaluator.commons.interfaces.ParametrizedElementInspector;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationFlags.correctImplementation.CorrectlyImplementedConstructor;
import com.github.deb4cker.vic.evaluator.implementationFlags.factory.impl.ConstructorFlagFactory;
import com.github.deb4cker.vic.evaluator.implementationFlags.factory.impl.ConstructorParameterFlagFactory;
import com.github.deb4cker.vic.evaluator.inspectors.abstracts.AbstractInspector;
import com.github.deb4cker.vic.evaluator.inspectors.structures.ConstructorStructure;
import com.github.deb4cker.vic.evaluator.utils.InspectorRunner;
import com.github.deb4cker.vic.evaluator.utils.ReverseParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConstructorInspector extends AbstractInspector<Constructor<?>, ConstructorStructure> implements ParametrizedElementInspector
{
    public ConstructorInspector(Constructor<?>[] modeledConstructors, Constructor<?>[] submittedConstructors, String className) {
        super(new ConstructorFlagFactory(), modeledConstructors, submittedConstructors, className);
    }

    @Override
    protected List<ImplementationFlag> getInspectionResult() throws InterruptedException {
        List<ConstructorStructure> modeledConstructorsList = new ArrayList<>(modeledElements);
        List<ConstructorStructure> submittedConstructorsList = new ArrayList<>(submittedElements);

        List<ImplementationFlag> flags = checkOverloads(modeledConstructorsList, submittedConstructorsList);

        boolean totallyCorrect = ImplementationFlag.onlyCorrectFlagsIn(flags) && !modeledConstructorsList.isEmpty();
        if (totallyCorrect) flags.add(new CorrectlyImplementedConstructor());

        return flags;
    }

    @Override
    protected ConstructorStructure toStructure(Constructor<?> element) {
        String modifier = Modifier.toString(element.getModifiers());
        String name = element.getName();
        Parameter[] parameters = element.getParameters();

        return new ConstructorStructure(modifier, name, parameters);
    }

    @Override
    public List<ImplementationFlag> checkOverloads(List<ConstructorStructure> modeledElements, List<ConstructorStructure> submittedElements) throws InterruptedException {
        List<ImplementationFlag> flags = new ArrayList<>();
        for (ConstructorStructure modelConstructor : modeledElements) {
            Optional<ConstructorStructure> matchingConstructor = submittedElements.stream()
                    .filter(submittedConstructor -> constructorHasSameSignature(modelConstructor, submittedConstructor))
                    .findFirst();


            String completeConstructorSignature = ReverseParser.completeConstructorSignatureOf(modelConstructor);
            if(matchingConstructor.isEmpty()) {
                flags.add(flagFactory.createMissingInScopeFlag(completeConstructorSignature, completeConstructorSignature));
                break;
            }

            ParametersInspector inspector = new ParametersInspector(
                    new ConstructorParameterFlagFactory(),
                    modelConstructor.parameters(),
                    matchingConstructor.get().parameters(),
                    completeConstructorSignature
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

