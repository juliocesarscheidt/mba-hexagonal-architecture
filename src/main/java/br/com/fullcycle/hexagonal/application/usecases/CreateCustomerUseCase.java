package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.entities.Customer;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

public class CreateCustomerUseCase
	extends UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {
	
	public record Input(String cpf, String email, String name) {}

	public record Output(String id, String cpf, String email, String name) {}
	
	private final CustomerRepository customerRepository;
		
	public CreateCustomerUseCase(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public Output Execute(final Input input) throws ValidationException {
		if (customerRepository.customerOfCPF(input.cpf).isPresent()) {
            throw new ValidationException("Customer already exists");
        }

        if (customerRepository.customerOfEmail(input.email).isPresent()) {
        	throw new ValidationException("Customer already exists");
        }

        var customer = customerRepository.create(Customer.newCustomer(input.name, input.cpf, input.email));

        return new Output(customer.getCustomerId().value().toString(),
        	customer.getCpf(), customer.getEmail(), customer.getName());
	}
}
