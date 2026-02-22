package com.github.deb4cker.vic.vpl.cli;

import com.github.deb4cker.vic.evaluator.models.ClassData;
import com.github.deb4cker.vic.uxfreader.models.ParsedClassObject;
import com.github.deb4cker.vic.vpl.cli.pipeline.SubmissionVerifier;
import com.github.deb4cker.vic.vpl.cli.pipeline.TempFileWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
            String result = pipeline(null, List.of(fakeJava("Animal"))).run();
            assertTrue(result.contains("UXF") || result.contains("uxf") || result.contains("Nenhum"));
        }

        @Test
        @DisplayName("submittedJavaFiles vazio → retorna mensagem de erro")
        void emptySubmittedFilesReturnsErrorMessage() {
            assertFalse(pipeline(new File("diagram.uxf"), List.of()).run().isBlank());
        }
    }

    @Nested
    @DisplayName("SubmissionVerifier.verify()")
    class VerifySubmittedFiles {

        private SubmissionVerifier.VerificationResult verify(List<ClassData> modeled, List<File> submitted) {
            return SubmissionVerifier.verify(modeled, submitted);
        }

        @Test
        @DisplayName("todos os arquivos presentes → mensagem vazia e lista completa")
        void allPresentEmptyMessageAndFullList() {
            SubmissionVerifier.VerificationResult result = verify(List.of(new ClassData(Integer.class)),
                    List.of(fakeJava("Integer")));
            assertTrue(result.message().isEmpty());
            assertEquals(1, result.validSubmittedFiles().size());
        }

        @Test
        @DisplayName("arquivo faltando → mensagem contém nome da classe não submetida")
        void missingFileErrorMessageContainsClassName() {
            SubmissionVerifier.VerificationResult result = verify(List.of(new ClassData(Integer.class)), List.of());
            assertFalse(result.message().isEmpty());
            assertTrue(result.message().contains("Integer"));
        }

        @Test
        @DisplayName("arquivo faltando → mensagem começa com 'Algumas classes não foram submetidas'")
        void missingFileMessagePrefix() {
            SubmissionVerifier.VerificationResult result = verify(List.of(new ClassData(Integer.class)), List.of());
            assertTrue(result.message().contains("Algumas classes não foram submetidas"));
        }

        @Test
        @DisplayName("match parcial → validFiles só contém o arquivo que foi submetido")
        void partialMatchOnlyValidFilesReturned() {
            SubmissionVerifier.VerificationResult result = verify(
                    List.of(new ClassData(Integer.class), new ClassData(String.class)),
                    List.of(fakeJava("String")));
            assertEquals(1, result.validSubmittedFiles().size());
            assertEquals("String.java", result.validSubmittedFiles().get(0).getName());
        }

        @Test
        @DisplayName("nenhum arquivo submetido → validFiles vazio")
        void noSubmittedAllMissingEmptyValidFiles() {
            SubmissionVerifier.VerificationResult result = verify(
                    List.of(new ClassData(Integer.class), new ClassData(String.class)), List.of());
            assertTrue(result.validSubmittedFiles().isEmpty());
        }

        @Test
        @DisplayName("sem classes modeladas → validFiles vazio e mensagem vazia")
        void noModeledClassesReturnsEmptyResult() {
            SubmissionVerifier.VerificationResult result = verify(List.of(), List.of(fakeJava("Animal")));
            assertTrue(result.validSubmittedFiles().isEmpty());
            assertTrue(result.message().isEmpty());
        }

        @Test
        @DisplayName("múltiplas classes faltando → todas aparecem na mensagem")
        void multipleMissingClassesAllInMessage() {
            SubmissionVerifier.VerificationResult result = verify(
                    List.of(new ClassData(Integer.class), new ClassData(String.class)), List.of());
            assertTrue(result.message().contains("Integer"));
            assertTrue(result.message().contains("String"));
        }
    }

    @Nested
    @DisplayName("SubmissionVerifier.VerificationResult (record)")
    class VerificationResultRecord {

        @Test
        @DisplayName("acessores validSubmittedFiles() e message() retornam o que foi passado")
        void recordAccessors() {
            File f = fakeJava("X");
            SubmissionVerifier.VerificationResult r = new SubmissionVerifier.VerificationResult(List.of(f), "erro");
            assertEquals(1, r.validSubmittedFiles().size());
            assertEquals("erro", r.message());
        }
    }

    @Nested
    @DisplayName("TempFileWriter.write()")
    class TempFilesWrite {

        @Test
        @DisplayName("lista vazia → retorna lista vazia")
        void emptyListReturnsEmptyList() {
            assertTrue(TempFileWriter.write(List.of()).isEmpty());
        }

        @Test
        @DisplayName("um ParsedClassObject → cria um arquivo .java no diretório temp")
        void singleParsedObjectCreatesOneTempFile() {
            List<File> result = TempFileWriter
                    .write(List.of(new ParsedClassObject("Animal", "public class Animal {}")));
            assertEquals(1, result.size());
            assertTrue(result.get(0).getName().startsWith("Animal"));
            assertTrue(result.get(0).getName().endsWith(".java"));
            assertTrue(result.get(0).getAbsolutePath()
                    .startsWith(Paths.get(System.getProperty("java.io.tmpdir")).toAbsolutePath().toString()));
            result.get(0).deleteOnExit();
        }

        @Test
        @DisplayName("múltiplos ParsedClassObjects → cria um arquivo por objeto")
        void multipleParsedObjectsCreateMultipleFiles() {
            List<File> result = TempFileWriter.write(List.of(
                    new ParsedClassObject("Animal", "public class Animal {}"),
                    new ParsedClassObject("Pessoa", "public class Pessoa {}")));
            assertEquals(2, result.size());
            result.forEach(File::deleteOnExit);
        }
    }
}
