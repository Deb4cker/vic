package com.github.deb4cker.vic.evaluator.implementationflags.factory.impl;

import com.github.deb4cker.vic.evaluator.implementationflags.factory.interfaces.InconsistencyFlagFactory;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.ImplementationInconsistency;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.attribute.IncorrectAttributeModifierInconsistency;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.attribute.IncorrectAttributeTypeInconsistency;
import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.attribute.MissingAttributeInClassInconsistency;

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
