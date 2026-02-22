package com.github.deb4cker.vic.uxfreader.parser.uxfparser;

import com.github.deb4cker.vic.commons.EvaluationBypassCode;
import com.github.deb4cker.vic.commons.StringUtils;
import com.github.deb4cker.vic.uxfreader.exception.DiagramSyntaxErrorException;
import com.github.deb4cker.vic.uxfreader.models.ParsedClassObject;
import com.github.deb4cker.vic.uxfreader.parser.ClassPanel;
import com.github.deb4cker.vic.uxfreader.parser.Relation;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public final class ClassCodeGenerator {

    public static ParsedClassObject generate(
            ClassPanel panel,
            Map<String, ClassPanel> classes
    ) throws DiagramSyntaxErrorException
    {
        CompilationUnit compilationUnit = new CompilationUnit();
        String className = panel.getClassName();

        ClassOrInterfaceDeclaration classDeclaration = compilationUnit.addClass(
                className,
                Stream.of(Modifier.Keyword.PUBLIC, panel.isAbstract() ? Modifier.Keyword.ABSTRACT : null)
                        .filter(Objects::nonNull)
                        .toArray(Modifier.Keyword[]::new));

        String superClassParams = null;

        for (Relation relation : panel.getTargets()) {
            switch (relation.type()) {
                case INHERITANCE -> superClassParams = applyInheritance(relation, classDeclaration, classes);
                case TO_MANY_RELATION -> applyToManyRelation(relation, panel, classDeclaration, compilationUnit);
                case TO_ONE_RELATION -> {
                    Modifier.Keyword modifier = panel.isAbstract() ? Modifier.Keyword.PROTECTED
                            : Modifier.Keyword.PRIVATE;
                    classDeclaration.addField(relation.className(), relation.className().toLowerCase(), modifier);
                }
            }
        }

        for (String line : panel.getAttributeSectionLines()) {
            FieldParser.parseField(line, classDeclaration);
        }

        for (String line : panel.getMethodSectionLines()) {
            MethodParser.parseMethodOrConstructor(line.trim(), classDeclaration, className, superClassParams);
        }

        return new ParsedClassObject(className, compilationUnit.toString().trim());
    }

    private static String applyInheritance(
            Relation relation,
            ClassOrInterfaceDeclaration classDeclaration,
            Map<String, ClassPanel> classes)
    {
        String superClassName = relation.className();
        classDeclaration.addExtendedType(superClassName);

        return Arrays.stream(classes.get(superClassName).getMethodSectionLines())
                .filter(line -> line.contains(superClassName.concat("(")))
                .findFirst()
                .map(line -> {
                    Matcher matcher = JavaSyntaxHelper.findMethodMatcher(line);
                    return matcher.find() ? matcher.group(3) : null;
                })
                .orElse(null);
    }

    private static void applyToManyRelation(
            Relation relation,
            ClassPanel panel,
            ClassOrInterfaceDeclaration classDeclaration,
            CompilationUnit compilationUnit)
    {
        String relationClassName = relation.className();
        String pluralName = StringUtils.pluralize(relationClassName).toLowerCase();

        ClassOrInterfaceType listType = new ClassOrInterfaceType(null, "ArrayList");
        listType.setTypeArguments(new ClassOrInterfaceType(null, relationClassName));

        Modifier.Keyword modifier = panel.isAbstract() ? Modifier.Keyword.PROTECTED : Modifier.Keyword.PRIVATE;
        classDeclaration.addField(listType, EvaluationBypassCode.generateEvaluationBypassCode(), modifier);
        compilationUnit.addImport("java.util.ArrayList");

        MethodDeclaration addMethod = classDeclaration.addMethod("add" + relationClassName, Modifier.Keyword.PUBLIC);
        addMethod.addParameter(new Parameter(new ClassOrInterfaceType(null, relationClassName), EvaluationBypassCode.generateEvaluationBypassCode()));

        MethodDeclaration getMethod = classDeclaration.addMethod("get" + StringUtils.capitalize(pluralName), Modifier.Keyword.PUBLIC);
        getMethod.setType(listType);
        getMethod.setBody(new BlockStmt().addStatement("return null;"));
    }

    private ClassCodeGenerator() {
    }
}
