package com.github.deb4cker.vic.evaluator.implementationflags.factory.interfaces;

import com.github.deb4cker.vic.evaluator.implementationflags.inconsistency.ImplementationInconsistency;

public interface InconsistencyFlagFactory
{
    ImplementationInconsistency createMissingInScopeFlag(String elementName, String parentName, String... args);

    ImplementationInconsistency createIncorrectTypeFlag(String elementName, String expectedType, String... args);

    ImplementationInconsistency createIncorrectModifierFlag(String elementName, String expectedModifier, String... args);
}
