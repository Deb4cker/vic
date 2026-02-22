package com.github.deb4cker.vic.evaluator.implementationFlags;

import com.github.deb4cker.vic.evaluator.implementationFlags.correctImplementation.CorrectImplementation;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ImplementationFlag")
class ImplementationFlagTest {

    private CorrectImplementation correct(String text) {
        return new CorrectImplementation(text) {
        };
    }

    private ImplementationInconsistency inconsistency(String text) {
        return new ImplementationInconsistency(text) {
        };
    }

    @Nested
    @DisplayName("isCorrectFlag")
    class IsCorrectFlag {

        @Test
        @DisplayName("retorna true para instâncias de CorrectImplementation")
        void trueForCorrectImplementation() {
            assertTrue(ImplementationFlag.isCorrectFlag(correct("ok")));
        }

        @Test
        @DisplayName("retorna false para ImplementationInconsistency")
        void falseForInconsistency() {
            assertFalse(ImplementationFlag.isCorrectFlag(inconsistency("erro")));
        }
    }

    @Nested
    @DisplayName("onlyCorrectFlagsIn")
    class OnlyCorrectFlagsIn {

        @Test
        @DisplayName("retorna true quando lista só tem flags corretas")
        void allCorrect() {
            List<ImplementationFlag> flags = List.of(correct("a"), correct("b"));
            assertTrue(ImplementationFlag.onlyCorrectFlagsIn(flags));
        }

        @Test
        @DisplayName("retorna false quando há ao menos uma inconsistência")
        void hasInconsistency() {
            List<ImplementationFlag> flags = List.of(correct("a"), inconsistency("x"));
            assertFalse(ImplementationFlag.onlyCorrectFlagsIn(flags));
        }

        @Test
        @DisplayName("retorna true para lista vazia")
        void emptyListReturnsTrue() {
            assertTrue(ImplementationFlag.onlyCorrectFlagsIn(List.of()));
        }
    }

    @Nested
    @DisplayName("hasInconsistencyFlagsIn")
    class HasInconsistencyFlagsIn {

        @Test
        @DisplayName("retorna false quando lista só tem flags corretas")
        void noInconsistency() {
            List<ImplementationFlag> flags = List.of(correct("x"), correct("y"));
            assertFalse(ImplementationFlag.hasInconsistencyFlagsIn(flags));
        }

        @Test
        @DisplayName("retorna true quando há ao menos uma inconsistência")
        void withInconsistency() {
            List<ImplementationFlag> flags = List.of(correct("x"), inconsistency("y"));
            assertTrue(ImplementationFlag.hasInconsistencyFlagsIn(flags));
        }

        @Test
        @DisplayName("retorna false para lista vazia")
        void emptyReturnsFalse() {
            assertFalse(ImplementationFlag.hasInconsistencyFlagsIn(List.of()));
        }
    }

    @Nested
    @DisplayName("toString — texto formatado com símbolo")
    class ToStringFormat {

        @Test
        @DisplayName("flag correta inclui símbolo de correto no texto")
        void correctFlagContainsSymbol() {
            String text = correct("Atributo nome").toString();
            assertFalse(text.isBlank());
            assertTrue(text.contains("Atributo nome"));
        }

        @Test
        @DisplayName("inconsistência inclui o texto de erro")
        void inconsistencyContainsErrorText() {
            String text = inconsistency("atributo faltando").toString();
            assertTrue(text.contains("atributo faltando"));
        }
    }

    @Nested
    @DisplayName("equals e hashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("duas flags com mesmo texto são iguais")
        void sameTextEquals() {
            CorrectImplementation a = correct("ok");
            CorrectImplementation b = correct("ok");
            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("flags com textos diferentes não são iguais")
        void differentTextNotEquals() {
            assertNotEquals(correct("a"), correct("b"));
        }

        @Test
        @DisplayName("flag correta != flag de inconsistência mesmo com texto similar")
        void differentTypesNotEqual() {
            assertNotEquals(correct("msg"), inconsistency("msg"));
        }
    }
}
