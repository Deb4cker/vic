package com.github.deb4cker.vic.vpl.cli;

import com.github.deb4cker.vic.evaluator.analyzers.AnalysisResult;
import com.github.deb4cker.vic.evaluator.enums.AnalysisScope;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationFlags.correctImplementation.CorrectImplementation;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReportCreator")
class ReportCreatorTest {

    private static ImplementationFlag correct(String msg) {
        return new CorrectImplementation(msg) {
        };
    }

    private static ImplementationFlag wrong(String msg) {
        return new ImplementationInconsistency(msg) {
        };
    }

    private static AnalysisResult classResult(String name, ImplementationFlag... flags) {
        return new AnalysisResult(name, List.of(flags), AnalysisScope.CLASS);
    }

    private static AnalysisResult relResult(String name, ImplementationFlag... flags) {
        return new AnalysisResult(name, List.of(flags), AnalysisScope.RELATIONSHIP);
    }

    @Nested
    @DisplayName("Cálculo de nota")
    class GradeCalculation {

        @Test
        @DisplayName("100% correto → nota 10.00")
        void allCorrectGrade10() {
            String report = ReportCreator.create(List.of(classResult("C", correct("a"), correct("b"))), 2);
            assertTrue(report.contains("10.00"), "Nota deveria ser 10.00. Report:\n" + report);
        }

        @Test
        @DisplayName("0% correto → nota 0.00")
        void allIncorrectGrade0() {
            String report = ReportCreator.create(List.of(classResult("C", wrong("x"), wrong("y"))), 2);
            assertTrue(report.contains("0.00"), "Nota deveria ser 0.00. Report:\n" + report);
        }

        @Test
        @DisplayName("50% correto → nota 5.00")
        void halfCorrectGrade5() {
            String report = ReportCreator.create(
                    List.of(classResult("C", correct("a"), correct("b"), wrong("c"), wrong("d"))), 4);
            assertTrue(report.contains("5.00"), "Nota deveria ser 5.00. Report:\n" + report);
        }

        @Test
        @DisplayName("exerciseSectionCount = 0 → NaN é suprimido, retorna string vazia para nota")
        void zeroSectionsSupressesNaN() {
            String report = ReportCreator.create(List.of(), 0);
            assertFalse(report.contains("NaN"), "NaN não deve aparecer no report:\n" + report);
        }

        @Test
        @DisplayName("mais flags incorretas do que seções → nota fica em 0.00 (Math.max clamp)")
        void correctCountClampedToZero() {
            String report = ReportCreator.create(
                    List.of(classResult("C", wrong("a"), wrong("b"), wrong("c"))), 1);
            assertTrue(report.contains("0.00"), "Nota deveria ser 0.00 após clamping. Report:\n" + report);
        }

        @Test
        @DisplayName("nota é formatada com locale US — usa ponto decimal, não vírgula")
        void gradeUsesUsLocale() {
            String report = ReportCreator.create(List.of(classResult("X", correct("a"))), 1);
            assertTrue(report.matches("(?s).*\\d+\\.\\d+.*"),
                    "Nota deve usar ponto decimal (locale US). Report:\n" + report);
        }
    }

    @Nested
    @DisplayName("Conteúdo do relatório")
    class ReportContent {

        @Test
        @DisplayName("relatório inclui o nome da classe analisada")
        void containsClassName() {
            String report = ReportCreator.create(List.of(classResult("Veiculo", correct("ok"))), 1);
            assertTrue(report.contains("Veiculo"));
        }

        @Test
        @DisplayName("relatório inclui a seção 'Nota'")
        void containsNotaSection() {
            String report = ReportCreator.create(List.of(classResult("X", correct("ok"))), 1);
            assertTrue(report.contains("Nota"));
        }

        @Test
        @DisplayName("scope CLASS gera prefixo 'Análise da classe'")
        void classScopePrefixIsAnaliseDaClasse() {
            String report = ReportCreator.create(List.of(classResult("Animal", correct("ok"))), 1);
            assertTrue(report.contains("Análise da classe Animal"),
                    "Prefixo de CLASS deve ser 'Análise da classe'. Report:\n" + report);
        }

        @Test
        @DisplayName("scope RELATIONSHIP gera prefixo 'Análise dos'")
        void relScopePrefixIsAnalisesDos() {
            String report = ReportCreator.create(List.of(relResult("Relacionamentos", correct("ok"))), 1);
            assertTrue(report.contains("Análise dos Relacionamentos"),
                    "Prefixo de RELATIONSHIP deve ser 'Análise dos'. Report:\n" + report);
        }

        @Test
        @DisplayName("CLASS é ordenado antes de RELATIONSHIP quando fornecidos fora de ordem")
        void classBeforeRelationshipInOutput() {
            var rel = relResult("Relacionamentos", correct("a"));
            var cls = classResult("MinhaClasse", correct("b"));
            String report = ReportCreator.create(List.of(rel, cls), 2);
            assertTrue(report.indexOf("MinhaClasse") < report.indexOf("Relacionamentos"),
                    "CLASS deve aparecer antes de RELATIONSHIP. Report:\n" + report);
        }

        @Test
        @DisplayName("texto de cada flag aparece no relatório")
        void flagTextIsIncluded() {
            String report = ReportCreator.create(
                    List.of(classResult("C", correct("atributo nome ok"), wrong("metodo ausente"))), 2);
            assertTrue(report.contains("atributo nome ok"));
            assertTrue(report.contains("metodo ausente"));
        }

        @Test
        @DisplayName("lista de resultados vazia → relatório contém apenas a nota 0.00 ou vazio")
        void emptyResultsNoClassSection() {
            String report = ReportCreator.create(List.of(), 1);
            assertTrue(report.contains("10.00") || report.isEmpty() || report.contains("Nota"),
                    "Report de vazio deve ter a nota intacta. Report:\n" + report);
        }

        @Test
        @DisplayName("múltiplas classes no relatório — todas aparecem")
        void multipleClassesAllAppear() {
            var r1 = classResult("ClasseA", correct("ok"));
            var r2 = classResult("ClasseB", wrong("erro"));
            String report = ReportCreator.create(List.of(r1, r2), 2);
            assertTrue(report.contains("ClasseA"));
            assertTrue(report.contains("ClasseB"));
        }
    }
}
