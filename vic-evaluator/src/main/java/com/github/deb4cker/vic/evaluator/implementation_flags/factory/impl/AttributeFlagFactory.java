package com.github.deb4cker.vic.evaluator.implementation_flags.factory.impl;

import com.github.deb4cker.vic.evaluator.implementation_flags.factory.interfaces.InconsistencyFlagFactory;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.ImplementationInconsistency;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.attribute.IncorrectAttributeModifierInconsistency;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.attribute.IncorrectAttributeTypeInconsistency;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.attribute.MissingAttributeInClassInconsistency;

public class AttributeFlagFactory implements InconsistencyFlagFactory {
    @Override
    public ImplementationInconsistency createMissingInScopeFlag(String elementName, String parentName, String... args) {
        return new MissingAttributeInClassInconsistency(elementName, parentName);
    }

    @Override
    public ImplementationInconsistency createIncorrectTypeFlag(String elementName, String expectedType, String... args) {
        return new IncorrectAttributeTypeInconsistency(elementName, expectedType);
    }

    @Override
    public ImplementationInconsistency createIncorrectModifierFlag(String elementName, String expectedModifier, String... args) {
        return new IncorrectAttributeModifierInconsistency(elementName, expectedModifier);
    }
}
