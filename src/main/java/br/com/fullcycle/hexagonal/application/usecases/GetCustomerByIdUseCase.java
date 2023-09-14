package br.com.fullcycle.hexagonal.application.usecases;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.domain.CustomerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

public class GetCustomerByIdUseCase
	extends UseCase<GetCustomerByIdUseCase.Input, Optional<GetCustomerByIdUseCase.Output>> {

	public record Input(String id) {}

	public record Output(String id, String cpf, String email, String name) {}

	private final CustomerRepository customerRepository;

	public GetCustomerByIdUseCase(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public Optional<GetCustomerByIdUseCase.Output> Execute(final Input input) throws ValidationException {
		return customerRepository.customerOfId(CustomerId.with(input.id))
			.map(customer -> new Output(
				customer.getCustomerId().value(), 
				customer.getCpf().value(),
				customer.getEmail().value(),
				customer.getName().value()
			));
	}
}
