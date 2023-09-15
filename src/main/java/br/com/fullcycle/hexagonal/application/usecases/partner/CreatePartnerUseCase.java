package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.person.CNPJ;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.usecases.UseCase;

public class CreatePartnerUseCase
	extends UseCase<CreatePartnerUseCase.Input, CreatePartnerUseCase.Output> {

	public record Input(String cnpj, String email, String name) {}

	public record Output(String id, String cnpj, String email, String name) {}

	private final PartnerRepository partnerRepository;

	public CreatePartnerUseCase(PartnerRepository partnerRepository) {
		this.partnerRepository = partnerRepository;
	}

	@Override
	public CreatePartnerUseCase.Output Execute(final Input input) throws ValidationException {
		if (partnerRepository.partnerOfCNPJ(new CNPJ(input.cnpj)).isPresent()) {
            throw new ValidationException("Partner already exists");
        }

        if (partnerRepository.partnerOfEmail(new Email(input.email)).isPresent()) {
        	throw new ValidationException("Partner already exists");
        }

        final var partner = partnerRepository.create(Partner.newPartner(input.name, input.cnpj, input.email));

        return new Output(
    		partner.getPartnerId().value(),
    		partner.getCnpj().value(),
    		partner.getEmail().value(),
    		partner.getName().value()
    	);
	}
}