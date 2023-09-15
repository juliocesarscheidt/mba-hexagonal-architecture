package br.com.fullcycle.hexagonal.application.usecases.customer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;

public class CreateCustomerUseCaseIT extends IntegrationTest {
	
	@Autowired
	private CreateCustomerUseCase useCase;
	
	@Autowired
	private CustomerJpaRepository customerRepository;
	
	@BeforeEach
	void tearDown() {
		customerRepository.deleteAll();
	}
	
    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreateCustomer() throws ValidationException {
    	// tests steps:
    	// - given
    	final var expectedCPF = "12345678901";
    	final var expectedEmail = "john.doe@gmail.com";
    	final var expectedName = "John Doe";

    	final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    	// - when
    	/*
    	final var customerService = mock(CustomerService.class);
    	when(customerService.findByCpf(expectedCPF)).thenReturn(Optional.empty());
    	when(customerService.findByEmail(expectedEmail)).thenReturn(Optional.empty());
    	when(customerService.save(any())).thenAnswer(a -> {
    		var customer = a.getArgument(0, Customer.class);
    		customer.setId(UUID.randomUUID().getMostSignificantBits());
    		return customer;
    	}); // returns the first argument passed
		*/

    	final var output = useCase.Execute(createInput);
    	// System.out.println(output);
    	// Output[id=4097595946165289677, cpf=12345678901, email=john.doe@gmail.com, name=John Doe]
    	
    	// - then
    	Assertions.assertNotNull(output.id());
    	Assertions.assertEquals(expectedCPF, output.cpf());
    	Assertions.assertEquals(expectedEmail, output.email());
    	Assertions.assertEquals(expectedName, output.name());
    }
    
    @Test
    @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws ValidationException {
    	// tests steps:
    	// - given
    	final var expectedCPF = "12345678901";
    	final var expectedEmail = "john.doe@gmail.com";
    	final var expectedName = "John Doe";
    	final var expectedError = "Customer already exists";

    	createCustomer(expectedCPF, expectedEmail, expectedName);
    	// aCustomer.setId(UUID.randomUUID().getMostSignificantBits());

    	final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);
    	
    	// - when
    	/*
    	final var customerService = mock(CustomerService.class);
    	when(customerService.findByCpf(expectedCPF)).thenReturn(Optional.of(aCustomer));
		*/
    	
    	final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.Execute(createInput));
    	// System.out.println(actualException.getMessage());
    	// Customer already exists
	
    	// - then
    	Assertions.assertEquals(expectedError, actualException.getMessage());
    }
    
    @Test
    @DisplayName("Não deve cadastrar um cliente com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws ValidationException {
    	// tests steps:
    	// - given
    	final var expectedCPF = "12345678901";
    	final var expectedEmail = "john.doe@gmail.com";
    	final var expectedName = "John Doe";
    	final var expectedError = "Customer already exists";

    	createCustomer("90123456789", expectedEmail, expectedName);
    	// aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
    	
    	final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    	// - when
    	/*
    	final var customerService = mock(CustomerService.class);
    	when(customerService.findByEmail(expectedEmail)).thenReturn(Optional.of(aCustomer));
    	*/

    	final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.Execute(createInput));
    	// System.out.println(actualException.getMessage());
    	// Customer already exists
	
    	// - then
    	Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private CustomerEntity createCustomer(final String cpf, final String email, final String name) {
    	final var aCustomer = new CustomerEntity();
    	// aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
    	aCustomer.setCpf(cpf);
    	aCustomer.setEmail(email);
    	aCustomer.setName(name);

    	return customerRepository.save(aCustomer);
    }    
}
