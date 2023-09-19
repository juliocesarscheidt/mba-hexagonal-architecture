package br.com.fullcycle.domain.person;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.domain.exceptions.ValidationException;

public class CpfTest {

	@Test
    @DisplayName("Deve instanciar um CPF valido")
    public void testCreateCPF() {
        // given
        final var expectedCPF = "411.536.538-00";

        // when
        final var actualCpf = new CPF(expectedCPF);

        // then
        Assertions.assertEquals(expectedCPF, actualCpf.value());
    }

    @Test
    @DisplayName("Não deve instanciar um CPF invalido")
    public void testCreateCPFWithInvalidValue() {
        // given
        final var expectedError = "Invalid value for CPF";

        // when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new CPF("123456.789-01")
        );

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um CPF null")
    public void testCreateCPFWithNullValue() {
        // given
        final var expectedError = "Invalid value for CPF";

        // when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new CPF(null)
        );

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
}
