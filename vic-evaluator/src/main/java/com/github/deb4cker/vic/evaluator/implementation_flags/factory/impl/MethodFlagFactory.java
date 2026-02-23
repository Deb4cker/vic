package com.github.deb4cker.vic.evaluator.implementation_flags.factory.impl;

import com.github.deb4cker.vic.evaluator.implementation_flags.factory.interfaces.InconsistencyFlagFactory;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.ImplementationInconsistency;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.method.IncorrectMethodModifierInconsistency;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.method.IncorrectMethodReturnTypeInconsistency;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.method.MissingMethodInClassInconsistency;

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
