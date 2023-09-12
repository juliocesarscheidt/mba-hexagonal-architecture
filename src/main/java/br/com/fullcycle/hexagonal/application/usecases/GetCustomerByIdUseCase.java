package br.com.fullcycle.hexagonal.application.usecases;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;

public class GetCustomerByIdUseCase
	extends UseCase<GetCustomerByIdUseCase.Input, Optional<GetCustomerByIdUseCase.Output>> {

	public record Input(Long id) {}

	public record Output(Long id, String cpf, String email, String name) {}

	private final CustomerService customerService;

	public GetCustomerByIdUseCase(CustomerService customerService) {
		this.customerService = customerService;
	}

	@Override
	public Optional<GetCustomerByIdUseCase.Output> Execute(final Input input) throws ValidationException {
		return customerService.findById(input.id)
			.map(c -> new Output(c.getId(), c.getCpf(), c.getEmail(), c.getName()));
	}
}
