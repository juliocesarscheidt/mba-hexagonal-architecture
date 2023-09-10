package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.PartnerService;

public class CreatePartnerUseCase
	extends UseCase<CreatePartnerUseCase.Input, CreatePartnerUseCase.Output> {

	public record Input(String cnpj, String email, String name) {}

	public record Output(Long id, String cnpj, String email, String name) {}

	private final PartnerService partnerService;

	public CreatePartnerUseCase(PartnerService partnerService) {
		this.partnerService = partnerService;
	}

	@Override
	public CreatePartnerUseCase.Output Execute(final Input input) throws ValidationException {
		if (partnerService.findByCnpj(input.cnpj).isPresent()) {
            throw new ValidationException("Partner already exists");
        }

        if (partnerService.findByEmail(input.email).isPresent()) {
        	throw new ValidationException("Partner already exists");
        }

        var partner = new Partner();
        partner.setName(input.name);
        partner.setCnpj(input.cnpj);
        partner.setEmail(input.email);

        partner = partnerService.save(partner);

        return new Output(partner.getId(), partner.getCnpj(),
    		partner.getEmail(), partner.getName());
	}
}