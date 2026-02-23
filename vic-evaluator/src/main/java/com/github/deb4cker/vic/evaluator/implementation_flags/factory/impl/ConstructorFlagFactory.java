package com.github.deb4cker.vic.evaluator.implementation_flags.factory.impl;

import com.github.deb4cker.vic.evaluator.implementation_flags.factory.interfaces.InconsistencyFlagFactory;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.ImplementationInconsistency;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.constructor.IncorrectConstructorModifierInconsistency;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.constructor.MissingConstructorInClassInconsistency;

public class ConstructorFlagFactory implements InconsistencyFlagFactory {
    @Override
    public ImplementationInconsistency createMissingInScopeFlag(String elementName, String parentName, String... args) {
        return new MissingConstructorInClassInconsistency(parentName);
    }

    @Override
    public ImplementationInconsistency createIncorrectTypeFlag(String elementName, String expectedType, String... args) {
        return null;
    }

    @Override
    public ImplementationInconsistency createIncorrectModifierFlag(String elementName, String expectedModifier, String... args) {
        return new IncorrectConstructorModifierInconsistency(elementName, expectedModifier);
    }
}
