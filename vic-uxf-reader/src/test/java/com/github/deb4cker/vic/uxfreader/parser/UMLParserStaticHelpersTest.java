package com.github.deb4cker.vic.uxfreader.parser;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ReturnStmt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UMLParser — helpers estáticos")
class UMLParserStaticHelpersTest {

    @Nested
    @DisplayName("switchVisibility")
    class SwitchVisibility {

        private MethodDeclaration newMethod() {
            ClassOrInterfaceDeclaration cls = new ClassOrInterfaceDeclaration();
            cls.setName("Dummy");
            return cls.addMethod("teste");
        }

        @Test
        @DisplayName("'+' adiciona modificador PUBLIC")
        void plusAddsPublic() {
            MethodDeclaration m = newMethod();
            UMLParser.switchVisibility(m, "+");
            assertTrue(m.getModifiers().stream()
                    .anyMatch(mod -> mod.getKeyword() == Modifier.Keyword.PUBLIC));
        }

        @Test
        @DisplayName("'-' adiciona modificador PRIVATE")
        void minusAddsPrivate() {
            MethodDeclaration m = newMethod();
            UMLParser.switchVisibility(m, "-");
            assertTrue(m.getModifiers().stream()
                    .anyMatch(mod -> mod.getKeyword() == Modifier.Keyword.PRIVATE));
        }

        @Test
        @DisplayName("'#' adiciona modificador PROTECTED")
        void hashAddsProtected() {
            MethodDeclaration m = newMethod();
            UMLParser.switchVisibility(m, "#");
            assertTrue(m.getModifiers().stream()
                    .anyMatch(mod -> mod.getKeyword() == Modifier.Keyword.PROTECTED));
        }

        @Test
        @DisplayName("símbolo inválido lança IllegalArgumentException")
        void invalidSymbolThrows() {
            MethodDeclaration m = newMethod();
            assertThrows(IllegalArgumentException.class, () -> UMLParser.switchVisibility(m, "?"));
        }
    }

    @Nested
    @DisplayName("switchReturnStatement")
    class SwitchReturnStatement {

        @Test
        @DisplayName("'int' retorna ReturnStmt com IntegerLiteralExpr(0)")
        void intReturnsZero() {
            ReturnStmt stmt = UMLParser.switchReturnStatement("int");
            assertNotNull(stmt);
            assertTrue(stmt.toString().contains("0"),
                    "Esperado 'return 0;', mas foi: " + stmt);
        }

        @Test
        @DisplayName("'double' retorna ReturnStmt com DoubleLiteralExpr(0.0)")
        void doubleReturnsZeroPoint0() {
            ReturnStmt stmt = UMLParser.switchReturnStatement("double");
            assertTrue(stmt.toString().contains("0"),
                    "Esperado 'return 0.0;', mas foi: " + stmt);
        }

        @Test
        @DisplayName("'boolean' retorna ReturnStmt com false")
        void booleanReturnsFalse() {
            ReturnStmt stmt = UMLParser.switchReturnStatement("boolean");
            assertTrue(stmt.toString().contains("false"),
                    "Esperado 'return false;', mas foi: " + stmt);
        }

        @Test
        @DisplayName("'char' retorna ReturnStmt com literal char nulo")
        void charReturnsNullChar() {
            ReturnStmt stmt = UMLParser.switchReturnStatement("char");
            assertNotNull(stmt);
        }

        @Test
        @DisplayName("'String' (objeto) retorna ReturnStmt com null")
        void objectTypeReturnsNull() {
            ReturnStmt stmt = UMLParser.switchReturnStatement("String");
            assertTrue(stmt.toString().contains("null"),
                    "Esperado 'return null;', mas foi: " + stmt);
        }

        @Test
        @DisplayName("'long' retorna ReturnStmt com 0")
        void longReturnsZero() {
            ReturnStmt stmt = UMLParser.switchReturnStatement("long");
            assertTrue(stmt.toString().contains("0"),
                    "Esperado 'return 0;', mas foi: " + stmt);
        }

        @Test
        @DisplayName("'short' retorna ReturnStmt com 0")
        void shortReturnsZero() {
            ReturnStmt stmt = UMLParser.switchReturnStatement("short");
            assertTrue(stmt.toString().contains("0"));
        }
    }
}
