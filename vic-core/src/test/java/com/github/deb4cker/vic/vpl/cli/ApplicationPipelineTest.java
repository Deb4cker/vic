package com.github.deb4cker.vic.vpl.cli;

import com.github.deb4cker.vic.evaluator.models.ClassData;
import com.github.deb4cker.vic.uxfreader.models.ParsedClassObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ApplicationPipeline.
 *
 * Strategy:
 * - run() methods that need real UXF/compilation are covered by the
 * early-return
 * guard paths (null uxfFile, empty submittedFiles) — no I/O required.
 * - verifySubmittedFiles() and createTempFiles() are public methods that can
 * be called directly without invoking the full pipeline.
 * - VerificationResult (record) accessors are exercised via
 * verifySubmittedFiles().
 */
@DisplayName("ApplicationPipeline")
class ApplicationPipelineTest {

    private ApplicationPipeline pipeline(File uxf, List<File> javaFiles) {
        return new ApplicationPipeline(uxf, javaFiles);
    }

    private File fakeJava(String simpleName) {
        return new File(simpleName + ".java");
    }

    @Nested
    @DisplayName("run() — retornos antecipados")
    class RunGuards {

        @Test
        @DisplayName("uxfFile == null → retorna mensagem de erro")
        void nullUxfFileReturnsErrorMessage() {
            ApplicationPipeline p = pipeline(null, List.of(fakeJava("Animal")));
            String result = p.run();
            assertFalse(result.isBlank(),
                    "Deveria retornar uma mensagem de erro quando uxfFile é null");
            assertTrue(result.contains("UXF") || result.contains("uxf") || result.contains("Nenhum"),
                    "Mensagem deve mencionar arquivo UXF. Retorno: " + result);
        }

        @Test
        @DisplayName("submittedJavaFiles vazio → retorna mensagem de erro")
        void emptySubmittedFilesReturnsErrorMessage() {
            File dummyUxf = new File("diagram.uxf");
            ApplicationPipeline p = pipeline(dummyUxf, List.of());
            String result = p.run();
            assertFalse(result.isBlank(),
                    "Deveria retornar uma mensagem de erro quando não há arquivos submetidos");
        }
    }

    @Nested
    @DisplayName("verifySubmittedFiles()")
    class VerifySubmittedFiles {

        private ApplicationPipeline pipe() {
            return pipeline(null, List.of());
        }

        @Test
        @DisplayName("todos os arquivos presentes → mensagem vazia e lista completa")
        void allPresentEmptyMessageAndFullList() {
            ClassData intData = new ClassData(Integer.class);
            File intFile = fakeJava("Integer");

            ApplicationPipeline.VerificationResult result = pipe().verifySubmittedFiles(List.of(intData),
                    List.of(intFile));

            assertTrue(result.message().isEmpty(),
                    "Mensagem deve ser vazia quando todos os arquivos estão presentes");
            assertEquals(1, result.validSubmittedFiles().size());
        }

        @Test
        @DisplayName("arquivo faltando → mensagem contém nome da classe não submetida")
        void missingFileErrorMessageContainsClassName() {
            ClassData intData = new ClassData(Integer.class);

            ApplicationPipeline.VerificationResult result = pipe().verifySubmittedFiles(List.of(intData), List.of());

            assertFalse(result.message().isEmpty(),
                    "Mensagem deve indicar a classe faltante");
            assertTrue(result.message().contains("Integer"),
                    "Mensagem deve conter 'Integer'. Foi: " + result.message());
        }

        @Test
        @DisplayName("arquivo faltando → mensagem começa com 'Algumas classes não foram submetidas'")
        void missingFileMessagePrefix() {
            ClassData intData = new ClassData(Integer.class);

            ApplicationPipeline.VerificationResult result = pipe().verifySubmittedFiles(List.of(intData), List.of());

            assertTrue(result.message().contains("Algumas classes não foram submetidas"),
                    "Prefixo da mensagem incorreto. Mensagem: " + result.message());
        }

        @Test
        @DisplayName("match parcial → validFiles só contém o arquivo que foi submetido")
        void partialMatchOnlyValidFilesReturned() {
            ClassData intData = new ClassData(Integer.class);
            ClassData stringData = new ClassData(String.class);
            File stringFile = fakeJava("String");

            ApplicationPipeline.VerificationResult result = pipe().verifySubmittedFiles(List.of(intData, stringData),
                    List.of(stringFile));

            assertEquals(1, result.validSubmittedFiles().size());
            assertEquals("String.java", result.validSubmittedFiles().get(0).getName());
        }

        @Test
        @DisplayName("nenhum arquivo submetido → validFiles vazio")
        void noSubmittedAllMissingEmptyValidFiles() {
            ClassData a = new ClassData(Integer.class);
            ClassData b = new ClassData(String.class);

            ApplicationPipeline.VerificationResult result = pipe().verifySubmittedFiles(List.of(a, b), List.of());

            assertTrue(result.validSubmittedFiles().isEmpty());
        }

        @Test
        @DisplayName("sem classes modeladas → validFiles vazio e mensagem vazia")
        void noModeledClassesReturnsEmptyResult() {
            ApplicationPipeline.VerificationResult result = pipe().verifySubmittedFiles(List.of(),
                    List.of(fakeJava("Animal")));

            assertTrue(result.validSubmittedFiles().isEmpty());
            assertTrue(result.message().isEmpty());
        }

        @Test
        @DisplayName("múltiplas classes faltando → todas aparecem na mensagem")
        void multipleMissingClassesAllInMessage() {
            ClassData a = new ClassData(Integer.class);
            ClassData b = new ClassData(String.class);

            ApplicationPipeline.VerificationResult result = pipe().verifySubmittedFiles(List.of(a, b), List.of());

            assertTrue(result.message().contains("Integer"),
                    "Mensagem deve conter Integer");
            assertTrue(result.message().contains("String"),
                    "Mensagem deve conter String");
        }
    }

    @Nested
    @DisplayName("VerificationResult (record)")
    class VerificationResultRecord {

        @Test
        @DisplayName("acessores validSubmittedFiles() e message() retornam o que foi passado")
        void recordAccessors() {
            File f = fakeJava("X");
            ApplicationPipeline.VerificationResult r = new ApplicationPipeline.VerificationResult(List.of(f), "erro");

            assertEquals(1, r.validSubmittedFiles().size());
            assertEquals("erro", r.message());
        }
    }

    @Nested
    @DisplayName("createTempFiles()")
    class CreateTempFiles {

        @Test
        @DisplayName("lista vazia → retorna lista vazia")
        void emptyListReturnsEmptyList() {
            ApplicationPipeline p = pipeline(null, List.of());
            List<File> result = p.createTempFiles(List.of());
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("um ParsedClassObject → cria um arquivo .java no diretório temp")
        void singleParsedObjectCreatesOneTempFile() {
            ParsedClassObject parsed = new ParsedClassObject("Animal", "public class Animal {}");

            ApplicationPipeline p = pipeline(null, List.of());
            List<File> result = p.createTempFiles(List.of(parsed));

            assertEquals(1, result.size());
            File created = result.get(0);
            assertTrue(created.getName().endsWith(".java"),
                    "O arquivo temp deveria ter extensão .java. Nome: " + created.getName());
            assertTrue(created.getName().startsWith("Animal"),
                    "O arquivo temp deveria começar com o nome da classe. Nome: " + created.getName());
            String tmpDir = System.getProperty("java.io.tmpdir");
            assertTrue(created.getAbsolutePath().startsWith(Paths.get(tmpDir).toAbsolutePath().toString()),
                    "O arquivo deveria estar em java.io.tmpdir");

            created.deleteOnExit();
        }

        @Test
        @DisplayName("múltiplos ParsedClassObjects → cria um arquivo por objeto")
        void multipleParsedObjectsCreateMultipleFiles() {
            ParsedClassObject p1 = new ParsedClassObject("Animal", "public class Animal {}");
            ParsedClassObject p2 = new ParsedClassObject("Pessoa", "public class Pessoa {}");

            ApplicationPipeline p = pipeline(null, List.of());
            List<File> result = p.createTempFiles(List.of(p1, p2));

            assertEquals(2, result.size());
            result.forEach(File::deleteOnExit);
        }
    }
}
