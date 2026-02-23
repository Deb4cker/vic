package com.github.deb4cker.vic.evaluator.implementation_flags.warning;

import com.github.deb4cker.vic.evaluator.commons.constants.Symbols;
import com.github.deb4cker.vic.evaluator.implementation_flags.ImplementationFlag;

public class ImplementationWarning extends ImplementationFlag {
	public ImplementationWarning(String errorText) {
		super(Symbols.WARNING_SYMBOL, errorText);
	}
}
