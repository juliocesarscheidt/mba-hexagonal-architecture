package br.com.fullcycle.hexagonal.application.usecases.customer;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.repositories.InMemoryCustomerRepository;

public class GetCustomerByIdUseCaseTest {

	@Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetCustomerById() throws Exception {
		// given
        final var expectedCPF = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

        final var customerRepository = new InMemoryCustomerRepository();
        customerRepository.create(aCustomer);

        final var expectedID = aCustomer.getCustomerId().value().toString();
        
        final var input = new GetCustomerByIdUseCase.Input(expectedID);

        // when
        final var useCase = new GetCustomerByIdUseCase(customerRepository);
        final var output = useCase.Execute(input).get();

        // then
        Assertions.assertEquals(expectedID, output.id());
        Assertions.assertEquals(expectedCPF, output.cpf());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }
	
	@Test
    @DisplayName("Deve obter vazio ao tentar recuperar um cliente nao existente")
    public void testGetInexistingCustomerById() throws Exception {
		// given
        final var expectedID = UUID.randomUUID().toString();

        final var input = new GetCustomerByIdUseCase.Input(expectedID);

        // when
        final var customerRepository = new InMemoryCustomerRepository();
        final var useCase = new GetCustomerByIdUseCase(customerRepository);
        final var output = useCase.Execute(input);

        // then
        Assertions.assertTrue(output.isEmpty());
    }
}
