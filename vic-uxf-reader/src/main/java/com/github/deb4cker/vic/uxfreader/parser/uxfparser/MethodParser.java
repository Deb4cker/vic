package com.github.deb4cker.vic.uxfreader.parser.uxfparser;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public final class MethodParser {

    private MethodParser() {
    }

    public static void parseMethodOrConstructor(String line, ClassOrInterfaceDeclaration classDeclaration,
            String className, String superClassParams) {
        boolean isAbstract = line.startsWith("/") && line.endsWith("/");
        Matcher matcher = JavaSyntaxHelper.findMethodMatcher(line);
        if (!matcher.find())
            return;

        String visibility = matcher.group(1);
        String methodName = matcher.group(2);
        String params = matcher.group(3);
        String returnType = matcher.group(4);
        boolean isConstructor = methodName.equals(className);

        if (isConstructor) {
            BlockStmt body = new BlockStmt();
            if (superClassParams != null) {
                String paramsText = String.join(",", parseParametersCall(superClassParams));
                body.addStatement("super(" + paramsText + ");");
            }
            CallableDeclaration<?> ctor = classDeclaration.addConstructor().setBody(body);
            parseParameters(params, ctor);
            JavaSyntaxHelper.switchVisibility(ctor, visibility);
            return;
        }

        MethodDeclaration method = classDeclaration.addMethod(methodName);
        parseParameters(params, method);
        JavaSyntaxHelper.switchVisibility(method, visibility);

        String type = returnType != null ? returnType : "void";
        method.setType(type);

        if (isAbstract) {
            method.setAbstract(true);
            method.removeBody();
            return;
        }

        addDefaultMethodBody(method, type);
    }

    private static void parseParameters(String params, CallableDeclaration<?> method) {
        if (params.isBlank())
            return;
        for (String param : params.split(",")) {
            String[] parts = param.trim().split("\\s*:\\s*");
            if (parts.length == 2) {
                method.addParameter(parts[1].trim(), parts[0].trim());
            }
        }
    }

    private static String[] parseParametersCall(String params) {
        List<String> result = new ArrayList<>();
        if (!params.isBlank()) {
            for (String param : params.split(",")) {
                String[] parts = param.trim().split(":");
                if (parts.length == 2)
                    result.add(parts[0].trim());
            }
        }
        return result.toArray(new String[0]);
    }

    private static void addDefaultMethodBody(MethodDeclaration method, String returnType) {
        if (returnType.equals("void")) {
            method.setBody(new BlockStmt());
            return;
        }
        ReturnStmt returnStmt = JavaSyntaxHelper.switchReturnStatement(returnType);
        method.setBody(new BlockStmt().addStatement(returnStmt));
    }
}
