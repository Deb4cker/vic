package com.github.deb4cker.vic.evaluator.exception;

/**
 * Thrown when an
 * {@link com.github.deb4cker.vic.evaluator.commons.interfaces.Inspector}
 * thread is interrupted during execution.
 */
public class InspectorExecutionException extends RuntimeException {

    public InspectorExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
