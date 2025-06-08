package com.github.deb4cker.vic.evaluator.implementationFlags.factory.interfaces;

import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;

public interface InconsistencyFlagFactory
{
    ImplementationInconsistency createMissingInScopeFlag(String elementName, String parentName, String... args);

    ImplementationInconsistency createIncorrectTypeFlag(String elementName, String expectedType, String... args);

    ImplementationInconsistency createIncorrectModifierFlag(String elementName, String expectedModifier, String... args);
}
