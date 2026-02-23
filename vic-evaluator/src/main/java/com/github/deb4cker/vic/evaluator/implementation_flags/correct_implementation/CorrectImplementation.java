package com.github.deb4cker.vic.evaluator.implementation_flags.correct_implementation;

import com.github.deb4cker.vic.evaluator.commons.constants.Symbols;
import com.github.deb4cker.vic.evaluator.implementation_flags.ImplementationFlag;

public class CorrectImplementation extends ImplementationFlag {
    protected CorrectImplementation(String text, Object... args) {
        super(Symbols.CORRECT_SYMBOL, text, args);
    }
}
