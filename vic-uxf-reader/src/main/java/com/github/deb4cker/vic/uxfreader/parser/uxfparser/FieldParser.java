package com.github.deb4cker.vic.uxfreader.parser.uxfparser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FieldParser {

    private static final Pattern FIELD_PATTERN = Pattern
            .compile("([+#-])\\s*(\\w+)\\s*:\\s*([\\w.$<>?,]+(?:\\[\\s*])*+)");

    private FieldParser() {
    }

    public static void parseField(String line, ClassOrInterfaceDeclaration classDeclaration) {
        Matcher matcher = FIELD_PATTERN.matcher(line);
        if (!matcher.find())
            return;

        String visibility = matcher.group(1);
        String fieldName = matcher.group(2);
        String fieldType = matcher.group(3);

        FieldDeclaration field = classDeclaration.addField(fieldType, fieldName);
        JavaSyntaxHelper.switchVisibility(field, visibility);
    }
}
