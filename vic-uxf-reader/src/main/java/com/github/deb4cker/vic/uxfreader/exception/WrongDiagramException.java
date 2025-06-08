package com.github.deb4cker.vic.uxfreader.exception;

public class WrongDiagramException extends RuntimeException{
    public WrongDiagramException(){
        super("Error found in diagram");
    }
}
