package com.github.deb4cker.vic.evaluator.implementationflags.factory.impl;

import com.github.deb4cker.vic.evaluator.implementationflags.factory.interfaces.ParameterFlagFactory;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.ImplementationInconsistency;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.parameter.IncorrectParameterTypeInConstructorInconsistency;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.parameter.MissingParameterInConstructorInconsistency;

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
