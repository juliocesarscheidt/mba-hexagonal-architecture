package br.com.fullcycle.hexagonal.application.usecases;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.infrastructure.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.repositories.CustomerRepository;

public class GetCustomerByIdUseCaseIT extends IntegrationTest {

	@Autowired
	private GetCustomerByIdUseCase useCase;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@BeforeEach
	void tearDown() {
		customerRepository.deleteAll();
	}
	
	@Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetCustomerById() throws Exception {
		// tests steps:
    	// - given
    	final var expectedCPF = "12345678901";
    	final var expectedEmail = "john.doe@gmail.com";
    	final var expectedName = "John Doe";

    	final var aCustomer = createCustomer(expectedCPF, expectedEmail, expectedName);
		final var expectedId = aCustomer.getId();

    	final var input = new GetCustomerByIdUseCase.Input(expectedId);

    	// - when
    	/*
    	final var customerService = mock(CustomerService.class);
    	when(customerService.findById(expectedId)).thenReturn(Optional.of(aCustomer));
    	*/

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
    	/*
    	final var customerService = mock(CustomerService.class);
    	when(customerService.findById(expectedId)).thenReturn(Optional.empty());
    	*/

    	final var output = useCase.Execute(input);
    	
    	// - then
    	Assertions.assertTrue(output.isEmpty());
    }

	private Customer createCustomer(final String cpf, final String email, final String name) {
    	final var aCustomer = new Customer();
    	// aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
    	aCustomer.setCpf(cpf);
    	aCustomer.setEmail(email);
    	aCustomer.setName(name);

    	return customerRepository.save(aCustomer);
    }
}
