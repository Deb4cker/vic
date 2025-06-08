package com.github.deb4cker.vic.uxfreader.exception;

public interface ThrowableException
{
    static void throwIf(boolean condition, Class<? extends ApplicationException> exceptionClass, String message) {
        if (condition) {
            try {
                throw exceptionClass.getDeclaredConstructor(String.class).newInstance(message);
            } catch (Exception e) {
                throw new ApplicationException("Failed to create exception instance");
            }
        }
    }
}
