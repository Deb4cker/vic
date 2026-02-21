package com.github.deb4cker.vic.commons;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EvaluationBypassCode")
class EvaluationBypassCodeTest {

    @Test
    @DisplayName("CODE contém apenas letras minúsculas (sem dígitos ou hífens)")
    void codeContainsOnlyLowercaseLetters() {
        String code = EvaluationBypassCode.CODE;
        assertNotNull(code);
        assertFalse(code.isEmpty(), "CODE não pode ser vazio");
        assertTrue(code.matches("[a-z]+"),
                "CODE deveria conter apenas letras minúsculas, mas foi: " + code);
    }

    @Test
    @DisplayName("generateEvaluationBypassCode() retorna string que começa com CODE")
    void generatedCodeStartsWithBaseCode() {
        String generated = EvaluationBypassCode.generateEvaluationBypassCode();
        assertTrue(generated.startsWith(EvaluationBypassCode.CODE),
                "O código gerado deveria começar com EvaluationBypassCode.CODE");
    }

    @Test
    @DisplayName("generateEvaluationBypassCode() incrementa initValue a cada chamada")
    void generatedCodeIncrementsEachCall() {
        String first = EvaluationBypassCode.generateEvaluationBypassCode();
        String second = EvaluationBypassCode.generateEvaluationBypassCode();


        int suffixFirst = extractSuffix(first);
        int suffixSecond = extractSuffix(second);

        assertEquals(suffixFirst + 1, suffixSecond,
                "O sufixo deveria incrementar a cada chamada");
    }

    @Test
    @DisplayName("duas chamadas de geração retornam strings diferentes")
    void eachGeneratedCodeIsUnique() {
        String first = EvaluationBypassCode.generateEvaluationBypassCode();
        String second = EvaluationBypassCode.generateEvaluationBypassCode();
        assertNotEquals(first, second,
                "Cada código gerado deveria ser único (sufixo diferente)");
    }

    private int extractSuffix(String generated) {
        String suffix = generated.substring(EvaluationBypassCode.CODE.length());
        return Integer.parseInt(suffix);
    }
}
