package com.github.deb4cker.vic.evaluator.implementation_flags;

import com.github.deb4cker.vic.evaluator.implementation_flags.correct_implementation.CorrectImplementation;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.ImplementationInconsistency;
import com.github.deb4cker.vic.evaluator.implementation_flags.warning.ImplementationWarning;

import java.util.List;
import java.util.Objects;

public abstract class ImplementationFlag implements Comparable<ImplementationFlag> {

	private final String text;
	
	protected ImplementationFlag(String symbol, String text, Object... args) {
        String pattern = symbol + " " + text;
		this.text = String.format(pattern, args);
	}

	@Override
	public int compareTo(ImplementationFlag other) {
		if (this instanceof ImplementationWarning && !(other instanceof ImplementationWarning))
			return -1;
		if (!(this instanceof ImplementationWarning) && other instanceof ImplementationWarning)
			return 1;
		return 0;
	}

	@Override
	public String toString() {
		return text;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		ImplementationFlag that = (ImplementationFlag) o;
		return Objects.equals(text, that.text);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(text);
	}

	public static boolean onlyCorrectFlagsIn(List<ImplementationFlag> flags){
		return flags.stream().allMatch(ImplementationFlag::isCorrectFlag);
	}

	public static boolean isCorrectFlag(ImplementationFlag flag){
		return flag instanceof CorrectImplementation;
	}

	public static boolean hasInconsistencyFlagsIn(List<ImplementationFlag> flags){
		return flags.stream().anyMatch(ImplementationInconsistency.class::isInstance);
	}
}
