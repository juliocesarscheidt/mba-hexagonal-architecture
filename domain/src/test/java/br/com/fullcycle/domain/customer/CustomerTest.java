package br.com.fullcycle.domain.customer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.domain.exceptions.ValidationException;

public class CustomerTest {

	@Test
    @DisplayName("Deve instanciar um cliente")
    public void testInstantiateCustomer() {
        // given
        final var expectedCPF = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        // when
        final var output = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

        // then
        Assertions.assertNotNull(output.getCustomerId());
        Assertions.assertEquals(expectedCPF, output.getCpf().value());
        Assertions.assertEquals(expectedEmail, output.getEmail().value());
        Assertions.assertEquals(expectedName, output.getName().value());
    }
	
	@Test
    @DisplayName("Nao deve instanciar um cliente com cpf invalido")
    public void testInstantiateCustomerWithInvalidCpf() {
        // given
        final var expectedError = "Invalid value for CPF";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
        	Customer.newCustomer("John Doe", "12345678901", "john.doe@gmail.com");
        });

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
	
	@Test
    @DisplayName("Nao deve instanciar um cliente com nome invalido")
    public void testInstantiateCustomerWithInvalidName() {
        // given
        final var expectedError = "Invalid value for Name";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
        	Customer.newCustomer(null, "123.456.789-01", "john.doe@gmail.com");
        });

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
	
	@Test
    @DisplayName("Nao deve instanciar um cliente com email invalido")
    public void testInstantiateCustomerWithInvalidEmail() {
        // given
        final var expectedError = "Invalid value for Email";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
        	Customer.newCustomer("John Doe", "123.456.789-01", "john.doe.gmail.com");
        });

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
