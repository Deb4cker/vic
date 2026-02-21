package com.github.deb4cker.vic.commons;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StringUtils")
class StringUtilsTest {

    @Nested
    @DisplayName("nameWithoutExtension")
    class NameWithoutExtension {

        @Test
        @DisplayName("remove extensão de path Unix")
        void removesExtensionFromUnixPath() {
            assertEquals("Animal", StringUtils.nameWithoutExtension("/home/user/Animal.java"));
        }

        @Test
        @DisplayName("remove extensão de path Windows")
        void removesExtensionFromWindowsPath() {
            assertEquals("Pessoa", StringUtils.nameWithoutExtension("C:\\projetos\\Pessoa.java"));
        }

        @Test
        @DisplayName("retorna nome simples quando não há separador")
        void simpleFilenameWithExtension() {
            assertEquals("Carro", StringUtils.nameWithoutExtension("Carro.class"));
        }

        @Test
        @DisplayName("retorna string inteira quando não há ponto")
        void noExtensionReturnsFullString() {
            assertEquals("MinhaClasse", StringUtils.nameWithoutExtension("MinhaClasse"));
        }

        @Test
        @DisplayName("nome com múltiplos pontos — remove apenas o último")
        void fileWithMultipleDots() {
            assertEquals("archive.backup.v1", StringUtils.nameWithoutExtension("archive.backup.v1.zip"));
            assertEquals("foo.bar", StringUtils.nameWithoutExtension("foo.bar.baz"));
        }
    }

    @Nested
    @DisplayName("capitalize")
    class Capitalize {

        @Test
        @DisplayName("capitaliza primeira letra")
        void capitalizesFirstLetter() {
            assertEquals("Animal", StringUtils.capitalize("animal"));
        }

        @Test
        @DisplayName("não altera se já está capitalizado")
        void alreadyCapitalized() {
            assertEquals("Animal", StringUtils.capitalize("Animal"));
        }

        @Test
        @DisplayName("string vazia retorna ela mesma")
        void emptyStringReturnsEmpty() {
            assertEquals("", StringUtils.capitalize(""));
        }

        @Test
        @DisplayName("null retorna null")
        void nullReturnsNull() {
            assertNull(StringUtils.capitalize(null));
        }

        @Test
        @DisplayName("string de um caractere é capitalizada")
        void singleCharCapitalized() {
            assertEquals("A", StringUtils.capitalize("a"));
        }
    }

    @Nested
    @DisplayName("pluralize — PT-BR")
    class Pluralize {

        @Test
        @DisplayName("palavra terminada em vogal comum → +s")
        void endsInVowel() {
            assertEquals("gatos", StringUtils.pluralize("gato"));
        }

        @Test
        @DisplayName("palavra terminada em -r → +es")
        void endsInR() {
            assertEquals("professores", StringUtils.pluralize("professor"));
        }

        @Test
        @DisplayName("palavra terminada em -s → +es")
        void endsInS() {
            assertEquals("deuses", StringUtils.pluralize("deus"));
        }

        @Test
        @DisplayName("palavra terminada em -z → +es")
        void endsInZ() {
            assertEquals("luzes", StringUtils.pluralize("luz"));
        }

        @Test
        @DisplayName("palavra terminada em -m → troca m por ns")
        void endsInM() {
            assertEquals("homens", StringUtils.pluralize("homem"));
        }

        @Test
        @DisplayName("palavra terminada em -ão → ões")
        void endsInAo() {
            assertEquals("aviões", StringUtils.pluralize("avião"));
        }

        @Test
        @DisplayName("palavra terminada em -al → ais")
        void endsInAl() {
            assertEquals("animais", StringUtils.pluralize("animal"));
        }

        @Test
        @DisplayName("palavra terminada em -el → eis")
        void endsInEl() {
            assertEquals("papeis", StringUtils.pluralize("papel"));
        }

        @Test
        @DisplayName("palavra terminada em -il → is")
        void endsInIl() {
            assertEquals("barris", StringUtils.pluralize("barril"));
        }

        @Test
        @DisplayName("palavra terminada em -l (genérico) → is")
        void endsInLGeneric() {
            assertEquals("hais", StringUtils.pluralize("hal"));
        }

        @Test
        @DisplayName("palavra terminada em -n → +s (cobertura de L50-52)")
        void endsInN() {
            assertEquals("clowns", StringUtils.pluralize("clown"));
        }

        @Test
        @DisplayName("palavra terminada em -t → +s (cobertura de L50-52)")
        void endsInT() {
            assertEquals("bits", StringUtils.pluralize("bit"));
        }

        @Test
        @DisplayName("palavra terminada em -p → +s (cobertura de L50-52)")
        void endsInP() {
            assertEquals("maps", StringUtils.pluralize("map"));
        }

        @Test
        @DisplayName("null retorna null")
        void nullReturnsNull() {
            assertNull(StringUtils.pluralize(null));
        }

        @Test
        @DisplayName("string vazia retorna ela mesma")
        void emptyReturnsEmpty() {
            assertEquals("", StringUtils.pluralize(""));
        }

        @Test
        @DisplayName("palavra com espaços extras é trimada antes")
        void trimmedBeforePluralize() {
            assertEquals("gatos", StringUtils.pluralize("  gato  "));
        }
    }

    @Nested
    @DisplayName("onlyClassNameOf")
    class OnlyClassNameOf {
        @Test
        @DisplayName("O nome da classe deve ser o nome simples")
        void simpleNameIsReturned() {
            assertEquals("StringUtils", StringUtils.onlyClassNameOf(StringUtils.class));
        }
    }

}
