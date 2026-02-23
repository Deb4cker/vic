package com.github.deb4cker.vic.evaluator.implementation_flags.factory.interfaces;

import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.ImplementationInconsistency;

public interface InconsistencyFlagFactory
{
    ImplementationInconsistency createMissingInScopeFlag(String elementName, String parentName, String... args);

    ImplementationInconsistency createIncorrectTypeFlag(String elementName, String expectedType, String... args);

    ImplementationInconsistency createIncorrectModifierFlag(String elementName, String expectedModifier, String... args);
}
