package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;
import jakarta.xml.bind.ValidationException;

public class CreateCustomerUseCase
	extends UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {
	
	public record Input(String cpf, String email, String name) {}

	public record Output(Long id, String cpf, String email, String name) {}
	
	private final CustomerService customerService;
		
	public CreateCustomerUseCase(CustomerService customerService) {
		this.customerService = customerService;
	}

	@Override
	public Output Execute(final Input input) throws ValidationException {
		if (customerService.findByCpf(input.cpf).isPresent()) {
            throw new ValidationException("Customer already exists");
        }

        if (customerService.findByEmail(input.email).isPresent()) {
        	throw new ValidationException("Customer already exists");
        }

        var customer = new Customer();
        customer.setName(input.name);
        customer.setCpf(input.cpf);
        customer.setEmail(input.email);

        customer = customerService.save(customer);

        return new Output(customer.getId(), customer.getCpf(),
    		customer.getEmail(), customer.getName());
	}
}