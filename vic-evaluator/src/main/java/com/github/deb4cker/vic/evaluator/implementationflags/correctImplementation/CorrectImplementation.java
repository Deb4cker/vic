package com.github.deb4cker.vic.evaluator.implementationflags.correctImplementation;

import com.github.deb4cker.vic.evaluator.commons.constants.Symbols;
import com.github.deb4cker.vic.evaluator.implementationflags.ImplementationFlag;

public class CorrectImplementation extends ImplementationFlag {
    protected CorrectImplementation(String text, Object... args) {
        super(Symbols.CORRECT_SYMBOL, text, args);
    }
}
