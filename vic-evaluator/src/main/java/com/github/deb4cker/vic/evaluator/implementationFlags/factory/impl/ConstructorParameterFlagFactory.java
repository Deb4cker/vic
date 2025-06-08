package com.github.deb4cker.vic.evaluator.implementationFlags.factory.impl;

import com.github.deb4cker.vic.evaluator.implementationFlags.factory.interfaces.ParameterFlagFactory;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.parameter.IncorrectParameterTypeInConstructorInconsistency;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.parameter.MissingParameterInConstructorInconsistency;

public class ConstructorParameterFlagFactory implements ParameterFlagFactory {
    @Override
    public ImplementationInconsistency createMissingInScopeFlag(String elementName, String parentName, String... args) {
        return new MissingParameterInConstructorInconsistency(elementName, parentName);
    }

    @Override
    public ImplementationInconsistency createIncorrectTypeFlag(String elementName, String expectedType, String... args) {
        return new IncorrectParameterTypeInConstructorInconsistency(elementName, expectedType);
    }

    @Override
    public ImplementationInconsistency createIncorrectModifierFlag(String elementName, String expectedModifier, String... args) {
        return null;
    }
}
