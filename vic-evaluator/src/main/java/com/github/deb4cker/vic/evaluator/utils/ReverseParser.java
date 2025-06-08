package com.github.deb4cker.vic.evaluator.utils;


import com.github.deb4cker.vic.evaluator.inspectors.structures.ConstructorStructure;
import com.github.deb4cker.vic.evaluator.inspectors.structures.MethodStructure;
import com.github.deb4cker.vic.commons.StringUtils;

import java.lang.reflect.Parameter;

public final class ReverseParser {
    public static String completeMethodSignatureOf(MethodStructure method) {
        StringBuilder result = new StringBuilder(method.name());
        result.append("(");

        if (method.parameters().length > 0) {
            for (Parameter parameter : method.parameters()) {
                result.append(parameter.getName())
                        .append(": ")
                        .append(StringUtils.onlyClassNameOf(parameter.getType()))
                        .append(", ");
            }
            result.delete(result.length() - 2, result.length());
        }
        result.append(")");

        if (method.returnType() != null) {
            result.append(": ").append(method.returnType());
        }

        return result.toString();
    }

    public static String completeConstructorSignatureOf(ConstructorStructure method) {
        StringBuilder result = new StringBuilder(method.name());
        result.append("(");

        if (method.parameters().length > 0) {
            for (Parameter parameter : method.parameters()) {
                result.append(parameter.getName())
                        .append(": ")
                        .append(StringUtils.onlyClassNameOf(parameter.getType()))
                        .append(", ");
            }
            result.delete(result.length() - 2, result.length());
        }

        result.append(")");
        return result.toString();
    }
}
