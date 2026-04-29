package com.github.deb4cker.vic.evaluator.implementation_flags.correct_implementation;

import com.github.deb4cker.vic.evaluator.commons.constants.Symbols;
import com.github.deb4cker.vic.evaluator.implementation_flags.ImplementationFlag;

public class CorrectlyImplementedOverloadedMethod extends ImplementationFlag {
    public CorrectlyImplementedOverloadedMethod(String signature) {
        super(Symbols.CORRECT_SYMBOL + " " + signature, "", new Object[0]);
    }
}
