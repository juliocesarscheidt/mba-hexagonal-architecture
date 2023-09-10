package br.com.fullcycle.hexagonal.application.usecases;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;

public class GetCustomerByIdUseCaseTest {

	@Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetCustomerById() throws Exception {
		// tests steps:
    	// - given
		final var expectedId = UUID.randomUUID().getMostSignificantBits();
    	final var expectedCPF = "12345678901";
    	final var expectedEmail = "john.doe@gmail.com";
    	final var expectedName = "John Doe";

    	final var aCustomer = new Customer();
    	aCustomer.setId(expectedId);
    	aCustomer.setCpf(expectedCPF);
    	aCustomer.setEmail(expectedEmail);
    	aCustomer.setName(expectedName);

    	final var input = new GetCustomerByIdUseCase.Input(expectedId);

    	// - when
    	final var customerService = mock(CustomerService.class);
    	when(customerService.findById(expectedId)).thenReturn(Optional.of(aCustomer));

    	final var useCase = new GetCustomerByIdUseCase(customerService);
    	final var output = useCase.Execute(input).get();
    	
    	// - then
    	Assertions.assertEquals(expectedId, output.id());
    	Assertions.assertEquals(expectedCPF, output.cpf());
    	Assertions.assertEquals(expectedEmail, output.email());
    	Assertions.assertEquals(expectedName, output.name());
    }
	
	@Test
    @DisplayName("Deve obter vazio ao tentar recuperar um cliente nao existente")
    public void testGetInexistingCustomerById() throws Exception {
		// tests steps:
    	// - given
		final var expectedId = UUID.randomUUID().getMostSignificantBits();

    	final var input = new GetCustomerByIdUseCase.Input(expectedId);

    	// - when
    	final var customerService = mock(CustomerService.class);
    	when(customerService.findById(expectedId)).thenReturn(Optional.empty());

    	final var useCase = new GetCustomerByIdUseCase(customerService);
    	final var output = useCase.Execute(input);
    	
    	// - then
    	Assertions.assertTrue(output.isEmpty());
    }
}
