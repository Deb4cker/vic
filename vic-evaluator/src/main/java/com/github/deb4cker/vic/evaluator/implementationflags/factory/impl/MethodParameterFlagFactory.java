package com.github.deb4cker.vic.evaluator.implementationflags.factory.impl;

import com.github.deb4cker.vic.evaluator.implementationflags.factory.interfaces.ParameterFlagFactory;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.ImplementationInconsistency;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.parameter.IncorrectParameterTypeInMethodInconsistency;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.parameter.MissingParameterInMethodInconsistency;

public class MethodParameterFlagFactory implements ParameterFlagFactory {
    @Override
    public ImplementationInconsistency createMissingInScopeFlag(String elementName, String parentName, String... args) {
        return new MissingParameterInMethodInconsistency(elementName, parentName);
    }

    @Override
    public ImplementationInconsistency createIncorrectTypeFlag(String elementName, String expectedType, String... args) {
        return new IncorrectParameterTypeInMethodInconsistency(elementName, expectedType, args[0]);
    }

    @Override
    public ImplementationInconsistency createIncorrectModifierFlag(String elementName, String expectedModifier, String... args) {
        return null;
    }
}
