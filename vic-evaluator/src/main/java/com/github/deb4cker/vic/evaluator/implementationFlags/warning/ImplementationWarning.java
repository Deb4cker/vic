package com.github.deb4cker.vic.evaluator.implementationFlags.warning;

import com.github.deb4cker.vic.evaluator.commons.constants.Symbols;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;

public class ImplementationWarning extends ImplementationFlag {
	public ImplementationWarning(String errorText) {
		super(Symbols.WARNING_SYMBOL, errorText);
	}
}
