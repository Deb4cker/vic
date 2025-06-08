package com.github.deb4cker.vic.uxfreader.exception;

public class DiagramSyntaxErrorException extends ApplicationException implements ThrowableException
{
    private static final String MESSAGE = "O diagrama parece estar incorreto!";

    public DiagramSyntaxErrorException(){
        super(MESSAGE);
    }

    public static void throwIf(boolean condition) {
        ThrowableException.throwIf(condition, DiagramSyntaxErrorException.class, MESSAGE);
    }
}
