package com.github.deb4cker.vic.evaluator.analyzers;

import com.github.deb4cker.vic.commons.enums.RelationType;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationFlags.correctImplementation.AllRelationshipsCorrectlyImplemented;
import com.github.deb4cker.vic.evaluator.implementationFlags.correctImplementation.CorrectlyImplementedRelationship;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.relationship.RelationshipNotImplementedInconsistency;
import com.github.deb4cker.vic.evaluator.models.ClassData;
import com.github.deb4cker.vic.evaluator.models.relations.One;
import com.github.deb4cker.vic.evaluator.models.relations.RelationshipData;
import com.github.deb4cker.vic.evaluator.models.relations.SubClass;
import com.github.deb4cker.vic.evaluator.models.relations.SuperClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RelationshipsAnalyzer")
class RelationshipsAnalyzerTest {

    /**
     * Cria um RelationshipData simples de herança entre dois nomes de classes.
     * Como ClassData recebe um Class<?> real, usamos tipos concretos existentes na
     * JDK
     * para representar os lados do relacionamento de forma leve.
     */
    private RelationshipData inheritance(Class<?> sub, Class<?> sup) {
        return new RelationshipData(
                new SubClass(new ClassData(sub)),
                new SuperClass(new ClassData(sup)),
                RelationType.INHERITANCE);
    }

    private RelationshipData oneToOne(Class<?> from, Class<?> to) {
        return new RelationshipData(
                new One(new ClassData(from)),
                new One(new ClassData(to)),
                RelationType.TO_ONE_RELATION);
    }

    @Nested
    @DisplayName("getRelationshipCount")
    class GetRelationshipCount {

        @Test
        @DisplayName("retorna o número de relacionamentos modelados")
        void countMatchesModeledSize() {
            var rel1 = inheritance(Integer.class, Number.class);
            var rel2 = oneToOne(String.class, Integer.class);

            RelationshipsAnalyzer analyzer = new RelationshipsAnalyzer(List.of(rel1, rel2), List.of());
            assertEquals(2, analyzer.getRelationshipCount());
        }

        @Test
        @DisplayName("retorna 0 para lista vazia")
        void countZeroForEmpty() {
            RelationshipsAnalyzer analyzer = new RelationshipsAnalyzer(List.of(), List.of());
            assertEquals(0, analyzer.getRelationshipCount());
        }
    }

    @Nested
    @DisplayName("analyze — relacionamento presente")
    class WhenRelationshipIsPresent {

        @Test
        @DisplayName("gera CorrectlyImplementedRelationship para cada relacionamento correto")
        void correctFlagForEachMatch() {
            var rel = inheritance(Integer.class, Number.class);
            RelationshipsAnalyzer analyzer = new RelationshipsAnalyzer(List.of(rel), List.of(rel));

            List<ImplementationFlag> flags = analyzer.analyze();

            boolean hasCorrect = flags.stream().anyMatch(CorrectlyImplementedRelationship.class::isInstance);
            assertTrue(hasCorrect, "Deveria conter CorrectlyImplementedRelationship");
        }

        @Test
        @DisplayName("adiciona AllRelationshipsCorrectlyImplemented quando todos corretos")
        void allRelationshipsCorrectFlagAdded() {
            var rel = inheritance(Integer.class, Number.class);
            RelationshipsAnalyzer analyzer = new RelationshipsAnalyzer(List.of(rel), List.of(rel));

            List<ImplementationFlag> flags = analyzer.analyze();

            boolean hasAllCorrect = flags.stream().anyMatch(AllRelationshipsCorrectlyImplemented.class::isInstance);
            assertTrue(hasAllCorrect, "Deveria conter AllRelationshipsCorrectlyImplemented");
        }
    }

    @Nested
    @DisplayName("analyze — relacionamento ausente")
    class WhenRelationshipIsMissing {

        @Test
        @DisplayName("gera RelationshipNotImplementedInconsistency para relacionamento não implementado")
        void inconsistencyFlagForMissingRelationship() {
            var modeled = inheritance(Integer.class, Number.class);
            RelationshipsAnalyzer analyzer = new RelationshipsAnalyzer(List.of(modeled), List.of());

            List<ImplementationFlag> flags = analyzer.analyze();

            boolean hasInconsistency = flags.stream()
                    .anyMatch(RelationshipNotImplementedInconsistency.class::isInstance);
            assertTrue(hasInconsistency, "Deveria conter RelationshipNotImplementedInconsistency");
        }

        @Test
        @DisplayName("NÃO adiciona AllRelationshipsCorrectlyImplemented quando há inconsistência")
        void noAllCorrectFlagWhenMissing() {
            var modeled = inheritance(Integer.class, Number.class);
            RelationshipsAnalyzer analyzer = new RelationshipsAnalyzer(List.of(modeled), List.of());

            List<ImplementationFlag> flags = analyzer.analyze();

            boolean hasAllCorrect = flags.stream().anyMatch(AllRelationshipsCorrectlyImplemented.class::isInstance);
            assertFalse(hasAllCorrect, "Não deveria conter AllRelationshipsCorrectlyImplemented");
        }
    }

    @Nested
    @DisplayName("analyze — lista vazia de modeled")
    class WhenNoModeledRelationships {

        @Test
        @DisplayName("retorna lista vazia de flags")
        void emptyFlagsForEmptyModeled() {
            RelationshipsAnalyzer analyzer = new RelationshipsAnalyzer(List.of(), List.of());
            List<ImplementationFlag> flags = analyzer.analyze();
            boolean hasNoInconsistency = flags.stream()
                    .noneMatch(RelationshipNotImplementedInconsistency.class::isInstance);
            assertTrue(hasNoInconsistency);
        }
    }
}
