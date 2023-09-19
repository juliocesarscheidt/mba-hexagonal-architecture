package br.com.fullcycle.application.partner;

import java.util.Optional;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.PartnerRepository;

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
