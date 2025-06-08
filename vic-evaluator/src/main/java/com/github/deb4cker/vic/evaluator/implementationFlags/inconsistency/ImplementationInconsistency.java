package com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency;

import com.github.deb4cker.vic.evaluator.constants.Symbols;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;

public class ImplementationInconsistency extends ImplementationFlag {
	public ImplementationInconsistency(String errorText, Object... args) {
		super(Symbols.INCONSISTENCY_SYMBOL, errorText, args);
	}
}
