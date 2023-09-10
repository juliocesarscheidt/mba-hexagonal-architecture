package br.com.fullcycle.hexagonal.application.usecases;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.services.PartnerService;

public class GetPartnerByIdUseCase
	extends UseCase<GetPartnerByIdUseCase.Input, Optional<GetPartnerByIdUseCase.Output>> {
	
	public record Input(Long id) {}

	public record Output(Long id, String cnpj, String email, String name) {}

	private final PartnerService partnerService;
	
	public GetPartnerByIdUseCase(PartnerService partnerService) {
		this.partnerService = partnerService;
	}

	@Override
	public Optional<GetPartnerByIdUseCase.Output> Execute(final Input input) throws ValidationException {
		return partnerService.findById(input.id)
			.map(p -> new Output(p.getId(), p.getCnpj(), p.getEmail(), p.getName()));
	}
}
