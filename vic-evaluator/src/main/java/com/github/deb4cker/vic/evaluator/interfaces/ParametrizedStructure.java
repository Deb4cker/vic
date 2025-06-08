package com.github.deb4cker.vic.evaluator.interfaces;

import java.lang.reflect.Parameter;
import java.util.Arrays;

public interface ParametrizedStructure {
    Parameter[] parameters();
    String name();

    default String[] getParametersType(Parameter[] parameters){
        return Arrays.stream(parameters)
                .map(parameter -> parameter.getType().getTypeName())
                .toArray(String[]::new);
    }
}
