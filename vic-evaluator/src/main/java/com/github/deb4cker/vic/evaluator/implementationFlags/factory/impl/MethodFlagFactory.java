package com.github.deb4cker.vic.evaluator.implementationFlags.factory.impl;

import com.github.deb4cker.vic.evaluator.implementationFlags.factory.interfaces.InconsistencyFlagFactory;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.method.IncorrectMethodModifierInconsistency;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.method.IncorrectMethodReturnTypeInconsistency;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.method.MissingMethodInClassInconsistency;

public class MethodFlagFactory implements InconsistencyFlagFactory {
    @Override
    public ImplementationInconsistency createMissingInScopeFlag(String elementName, String parentName, String... args) {
        return new MissingMethodInClassInconsistency(elementName, parentName);
    }

    @Override
    public ImplementationInconsistency createIncorrectTypeFlag(String elementName, String expectedType, String... args) {
        return new IncorrectMethodReturnTypeInconsistency(elementName, expectedType);
    }

    @Override
    public ImplementationInconsistency createIncorrectModifierFlag(String elementName, String expectedModifier, String... args) {
        return new IncorrectMethodModifierInconsistency(elementName, expectedModifier);
    }
}
