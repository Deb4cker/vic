package com.github.deb4cker.vic.uxfreader.parser;

import com.github.deb4cker.vic.uxfreader.diagram.Coordinates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClassPanel")
class ClassPanelTest {

    private Coordinates coords() {
        return new Coordinates(0, 0, 200, 150);
    }

    private String simplePanel(String className, String attributes, String methods) {
        return className + "\n--\n" + attributes + "\n--\n" + methods;
    }

    @Nested
    @DisplayName("Parsing básico")
    class BasicParsing {

        @Test
        @DisplayName("extrai o nome da classe do cabeçalho")
        void extractsClassName() {
            ClassPanel panel = new ClassPanel(simplePanel("Animal", "", ""), coords());
            assertEquals("Animal", panel.getClassName());
        }

        @Test
        @DisplayName("classe concreta — isAbstract é false")
        void concreteClassNotAbstract() {
            ClassPanel panel = new ClassPanel(simplePanel("Carro", "", ""), coords());
            assertFalse(panel.isAbstract());
        }

        @Test
        @DisplayName("classe abstrata — cabeçalho com {abstract} antes do nome")
        void abstractClassDetected() {
            String abstractPanel = "{abstract}\nForma\n--\n\n--\n";
            ClassPanel panel = new ClassPanel(abstractPanel, coords());
            assertTrue(panel.isAbstract());
            assertEquals("Forma", panel.getClassName());
        }

        @Test
        @DisplayName("parse com menos de 3 seções lança DiagramSyntaxErrorException")
        void throwsOnInvalidSectionCount() {
            Coordinates c = coords();
            assertThrows(RuntimeException.class, () -> new ClassPanel("Invalido\n--\nAtributos", c));
        }
    }

    @Nested
    @DisplayName("Seções de atributos e métodos")
    class Sections {

        @Test
        @DisplayName("linhas de atributos são extraídas corretamente")
        void attributeLinesExtracted() {
            String attrs = "- nome: String\n- idade: int";
            ClassPanel panel = new ClassPanel(simplePanel("Pessoa", attrs, ""), coords());
            String[] lines = panel.getAttributeSectionLines();
            boolean hasNome = false;
            for (String line : lines) {
                if (line.contains("nome")) {
                    hasNome = true;
                    break;
                }
            }
            assertTrue(hasNome, "Deveria conter linha do atributo 'nome'");
        }

        @Test
        @DisplayName("linhas de métodos são extraídas corretamente")
        void methodLinesExtracted() {
            String methods = "+ getNome(): String\n+ setNome(nome: String)";
            ClassPanel panel = new ClassPanel(simplePanel("Pessoa", "", methods), coords());
            String[] lines = panel.getMethodSectionLines();
            boolean hasGetNome = false;
            for (String line : lines) {
                if (line.contains("getNome")) {
                    hasGetNome = true;
                    break;
                }
            }
            assertTrue(hasGetNome, "Deveria conter linha do método 'getNome'");
        }

        @Test
        @DisplayName("getSections retorna exactamente 3 seções")
        void getSectionsReturnsThree() {
            ClassPanel panel = new ClassPanel(simplePanel("X", "f: int", "m()"), coords());
            assertEquals(3, panel.getSections().length);
        }
    }

    @Nested
    @DisplayName("Sources e Targets")
    class SourcesAndTargets {

        @Test
        @DisplayName("listas de sources e targets começam vazias")
        void initiallyEmpty() {
            ClassPanel panel = new ClassPanel(simplePanel("A", "", ""), coords());
            assertTrue(panel.getSources().isEmpty());
            assertTrue(panel.getTargets().isEmpty());
        }
    }
}
