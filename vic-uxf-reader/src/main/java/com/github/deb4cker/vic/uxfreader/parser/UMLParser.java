package com.github.deb4cker.vic.uxfreader.parser;

import com.github.deb4cker.vic.commons.EvaluationBypassCode;
import com.github.deb4cker.vic.uxfreader.diagram.*;
import com.github.deb4cker.vic.uxfreader.exception.DiagramSyntaxErrorException;
import com.github.deb4cker.vic.uxfreader.models.ParsedClassObject;
import com.github.deb4cker.vic.uxfreader.models.ParsedDiagram;
import com.github.deb4cker.vic.commons.StringUtils;
import com.github.deb4cker.vic.commons.interfaces.Loggable;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class UMLParser implements Loggable {

    private final Map<String, ClassPanel> classes = new HashMap<>();

    public ParsedDiagram parseDiagram(Diagram diagram){
        resolveClassPanels(diagram.getClassElements());
        loadConnections(diagram);

        List<ParsedClassObject> classes = this.classes.values().stream()
                .map(this::generateJavaClassFromUML)
                .toList();

        return new ParsedDiagram(classes);
    }

    private ParsedClassObject generateJavaClassFromUML(ClassPanel panel) throws DiagramSyntaxErrorException {
        CompilationUnit compilationUnit = new CompilationUnit();
        String className = panel.getClassName();

        ClassOrInterfaceDeclaration classDeclaration = compilationUnit.addClass(
                className,
                Stream.of(Modifier.Keyword.PUBLIC, panel.isAbstract() ? Modifier.Keyword.ABSTRACT : null)
                        .filter(Objects::nonNull)
                        .toArray(Modifier.Keyword[]::new)
        );

        String superClassParams = null;

        for (Relation relation : panel.getTargets()){
            switch (relation.type()) {
                case INHERITANCE -> {
                    String superClassName = relation.className();
                    classDeclaration.addExtendedType(superClassName);

                    String[] methodSectionLines = classes.get(superClassName).getMethodSectionLines();
                    for (String line : methodSectionLines){
                        if (!line.contains(superClassName.concat("("))) continue;

                        Matcher matcher = findMethodMatcher(line);
                        if (matcher.find()) superClassParams = matcher.group(3);
                        break;
                    }
                }
                case TO_MANY_RELATION -> {
                    String relationClassName = relation.className();
                    String pluralName = StringUtils.pluralize(relationClassName).toLowerCase();

                    ClassOrInterfaceType listType = new ClassOrInterfaceType(null, "ArrayList");
                    listType.setTypeArguments(new ClassOrInterfaceType(null, relationClassName));

                    Modifier.Keyword fieldModifier = panel.isAbstract() ? Modifier.Keyword.PROTECTED : Modifier.Keyword.PRIVATE;
                    classDeclaration.addField(listType, EvaluationBypassCode.generateEvaluationBypassCode(), fieldModifier);

                    compilationUnit.addImport("java.util.ArrayList");

                    MethodDeclaration addMethod = classDeclaration.addMethod("add" + relationClassName, Modifier.Keyword.PUBLIC);
                    Parameter param = new Parameter(new ClassOrInterfaceType(null, relationClassName), EvaluationBypassCode.generateEvaluationBypassCode());
                    addMethod.addParameter(param);

                    MethodDeclaration getMethod = classDeclaration.addMethod("get" +  StringUtils.capitalize(pluralName), Modifier.Keyword.PUBLIC);
                    getMethod.setType(listType);
                    getMethod.setBody(new BlockStmt().addStatement("return null;"));
                }
                case TO_ONE_RELATION -> {
                    Modifier.Keyword fieldModifier = panel.isAbstract() ? Modifier.Keyword.PROTECTED : Modifier.Keyword.PRIVATE;
                    classDeclaration.addField(relation.className(), relation.className().toLowerCase() ,fieldModifier);
                }
            }
        }

        for (String line : panel.getAttributeSectionLines()) {
            parseField(line, classDeclaration);
        }

        for (String line: panel.getMethodSectionLines()) {
            line = line.trim();
            parseMethodOrConstructor(line, classDeclaration, className, superClassParams);
        }

        final String classText = compilationUnit.toString().trim();

        return new ParsedClassObject(className, classText);
    }

    private void parseField(String line, ClassOrInterfaceDeclaration classDeclaration) {
        Pattern fieldPattern = Pattern.compile("([+#-])\\s*(\\w+)\\s*:\\s*([\\w.$<>?,]+(?:\\s*\\[\\s*])*)");        Matcher matcher       = fieldPattern.matcher(line);
        boolean patternFound  = matcher.find();

        if (!patternFound) return;

        String visibility = matcher.group(1);
        String fieldName  = matcher.group(2);
        String fieldType  = matcher.group(3);

        FieldDeclaration field = classDeclaration.addField(fieldType, fieldName);
        switchVisibility(field, visibility);
    }

    private void parseMethodOrConstructor(String line, ClassOrInterfaceDeclaration classDeclaration, String className, String superClassParams) {
        boolean isAbstract = line.startsWith("/") && line.endsWith("/");
        Matcher matcher = findMethodMatcher(line);
        boolean patternFound  = matcher.find();

        if (patternFound) {
            String visibility = matcher.group(1);
            String methodName = matcher.group(2);
            String params     = matcher.group(3);
            String returnType = matcher.group(4);
            boolean isConstructor = methodName.equals(className);

            CallableDeclaration<?> declaration;

            if (isConstructor) {
                BlockStmt body = new BlockStmt();
                if(superClassParams != null) {
                    String paramsText = String.join(",", parseParametersCall(superClassParams));
                    body.addStatement("super("+ paramsText +");");
                }
                 declaration = classDeclaration
                         .addConstructor()
                         .setBody(body);

                parseParameters(params, declaration);
                switchVisibility(declaration, visibility);
                return;
            }

            declaration = classDeclaration.addMethod(methodName);

            parseParameters(params, declaration);
            switchVisibility(declaration, visibility);

            MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
            String type = returnType != null ? returnType : "void";
            methodDeclaration.setType(type);

            if(isAbstract){
                methodDeclaration.setAbstract(true);
                methodDeclaration.removeBody();
                return;
            }

            addDefaultMethodBody(methodDeclaration, type);
        }
    }

    private void parseParameters(String params, CallableDeclaration<?> method) {
        if (!params.isBlank()) {
            String[] paramArray = params.split(",");
            for (String param : paramArray) {
                String[] paramParts = param.trim().split("\\s*:\\s*");
                if (paramParts.length == 2) {
                    String paramName = paramParts[0].trim();
                    String paramType = paramParts[1].trim();
                    method.addParameter(paramType, paramName);
                }
            }
        }
    }

    private String[] parseParametersCall(String params) {
        List<String> paramsList = new ArrayList<>();
        if (!params.isBlank()) {
            String[] paramArray = params.split(",");
            for (String param : paramArray) {
                String[] paramParts = param.trim().split(":");
                if (paramParts.length == 2) paramsList.add(paramParts[0].trim());
            }
        }
        return paramsList.toArray(new String[0]);
    }

    private void addDefaultMethodBody(MethodDeclaration method, String returnType) {
        if (returnType.equals("void")) {
            method.setBody(new BlockStmt());
            return;
        }

        BlockStmt body = new BlockStmt();
        ReturnStmt returnStmt = switchReturnStatement(returnType);

        body.addStatement(returnStmt);
        method.setBody(body);
    }

    public static void switchVisibility(NodeWithModifiers<?> node, String visibility) {
        switch (visibility) {
            case "+" -> node.addModifier(Modifier.Keyword.PUBLIC);
            case "-" -> node.addModifier(Modifier.Keyword.PRIVATE);
            case "#" -> node.addModifier(Modifier.Keyword.PROTECTED);
            default -> throw new IllegalArgumentException("Invalid visibility symbol: " + visibility);
        }
    }

    public static ReturnStmt switchReturnStatement(String returnType){
        return switch (returnType) {
            case "int", "short", "long", "byte" -> new ReturnStmt(new IntegerLiteralExpr(0));
            case "float", "double" -> new ReturnStmt(new DoubleLiteralExpr(0));
            case "boolean" -> new ReturnStmt(new BooleanLiteralExpr(false));
            case "char"    -> new ReturnStmt(new CharLiteralExpr('\u0000'));
            default        -> new ReturnStmt(new NullLiteralExpr());
        };
    }

    private Matcher findMethodMatcher(String line) {
        Pattern methodPattern = Pattern.compile(
                "([+#-])\\s*(\\w+)\\s*\\(\\s*(.*?)\\s*\\)\\s*:?\\s*([^\\s]+(?:\\s*\\[\\s*])*)?"
        );
        return methodPattern.matcher(line);
    }

    private void loadConnections(Diagram diagram) {
        for(RelationElement relationElement : diagram.getRelationElements()){
            RelationCoordinates relationCoordinates = new RelationCoordinates(relationElement);
            RelationPanel relationPanel = new RelationPanel(relationElement.getPanel_attributes(), relationCoordinates);
            Point start = relationCoordinates.getStartPoint();
            Point end = relationCoordinates.getEndPoint();

            ClassPanel source = null;
            ClassPanel target = null;

            for (ClassPanel panel : classes.values()) {

                if (isInside(panel.getCoordinates(), (int) start.x(), (int) start.y())) {
                    source = panel;
                }
                if (isInside(panel.getCoordinates(), (int) end.x(), (int) end.y())) {
                    target = panel;
                }
            }

            RelationConnection connection = new RelationConnection(source, target, relationPanel);
            connection.loadRelationInPanels();
        }
    }

    private boolean isInside(Coordinates coords, int x, int y) {
        return x >= coords.getX() &&
                x <= (coords.getX() + coords.getW()) &&
                y >= coords.getY() &&
                y <= (coords.getY() + coords.getH());
    }

    private void resolveClassPanels(List<ClassElement> classElements) {
        classElements.forEach(classElement -> {
            ClassPanel panel = new ClassPanel(classElement.getPanel_attributes(), classElement.getCoordinates());
            classes.put(panel.getClassName(), panel);
        });
    }
}

