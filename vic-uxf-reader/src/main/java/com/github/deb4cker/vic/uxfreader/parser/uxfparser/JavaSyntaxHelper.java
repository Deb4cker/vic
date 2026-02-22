package com.github.deb4cker.vic.uxfreader.parser.uxfparser;

import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JavaSyntaxHelper {

    private JavaSyntaxHelper() {
    }

    public static void switchVisibility(NodeWithModifiers<?> node, String visibility) {
        switch (visibility) {
            case "+" -> node.addModifier(Modifier.Keyword.PUBLIC);
            case "-" -> node.addModifier(Modifier.Keyword.PRIVATE);
            case "#" -> node.addModifier(Modifier.Keyword.PROTECTED);
            default -> throw new IllegalArgumentException("Invalid visibility symbol: " + visibility);
        }
    }

    public static ReturnStmt switchReturnStatement(String returnType) {
        return switch (returnType) {
            case "int", "short", "long", "byte" -> new ReturnStmt(new IntegerLiteralExpr("0"));
            case "float", "double" -> new ReturnStmt(new DoubleLiteralExpr(0));
            case "boolean" -> new ReturnStmt(new BooleanLiteralExpr(false));
            case "char" -> new ReturnStmt(new CharLiteralExpr('\u0000'));
            default -> new ReturnStmt(new NullLiteralExpr());
        };
    }

    static Matcher findMethodMatcher(String line) {
        Pattern methodPattern = Pattern.compile(
                "([+#-])\\s*(\\w+)\\s*\\(\\s*(.*?)\\s*\\)\\s*:?\\s*(\\S+(?:\\[\\s*])*+)?");
        return methodPattern.matcher(line);
    }
}
