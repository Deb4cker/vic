package com.github.deb4cker.vic.evaluator.implementationflags.inconsistency;

import com.github.deb4cker.vic.evaluator.commons.constants.Symbols;
import com.github.deb4cker.vic.evaluator.implementationflags.ImplementationFlag;

public class ImplementationInconsistency extends ImplementationFlag {
	public ImplementationInconsistency(String errorText, Object... args) {
		super(Symbols.INCONSISTENCY_SYMBOL, errorText, args);
	}
}
