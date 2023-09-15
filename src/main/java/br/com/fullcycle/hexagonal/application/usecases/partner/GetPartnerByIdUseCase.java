package br.com.fullcycle.hexagonal.application.usecases.partner;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.usecases.UseCase;

public class GetPartnerByIdUseCase
	extends UseCase<GetPartnerByIdUseCase.Input, Optional<GetPartnerByIdUseCase.Output>> {
	
	public record Input(String id) {}

	public record Output(String id, String cnpj, String email, String name) {}

	private final PartnerRepository partnerRepository;

	public GetPartnerByIdUseCase(PartnerRepository partnerRepository) {
		this.partnerRepository = partnerRepository;
	}
	
	@Override
	public Optional<GetPartnerByIdUseCase.Output> Execute(final Input input) throws ValidationException {
		return partnerRepository.partnerOfId(PartnerId.with(input.id))
			.map(partner -> new Output(
				partner.getPartnerId().value(), 
				partner.getCnpj().value(),
				partner.getEmail().value(),
				partner.getName().value()
			));
	}
}
