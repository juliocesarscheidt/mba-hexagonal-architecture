package br.com.fullcycle.application.customer;

import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.person.CPF;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.exceptions.ValidationException;

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
		if (customerRepository.customerOfCPF(new CPF(input.cpf)).isPresent()) {
            throw new ValidationException("Customer already exists");
        }

        if (customerRepository.customerOfEmail(new Email(input.email)).isPresent()) {
        	throw new ValidationException("Customer already exists");
        }

        var customer = customerRepository.create(Customer.newCustomer(input.name, input.cpf, input.email));

        return new Output(
    		customer.getCustomerId().value(),
        	customer.getCpf().value(),
        	customer.getEmail().value(),
        	customer.getName().value()
        );
	}
}
