package br.com.fullcycle.hexagonal.graphql;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.dtos.PartnerDTO;
import br.com.fullcycle.hexagonal.services.PartnerService;

//Adapter
@Controller
public class PartnerResolver {

	private final PartnerService partnerService;

	public PartnerResolver(PartnerService partnerService) {
		this.partnerService = Objects.requireNonNull(partnerService);
	}
	
	@MutationMapping
	public CreatePartnerUseCase.Output createPartner(@Argument PartnerDTO input) throws ValidationException {
        final var useCase = new CreatePartnerUseCase(partnerService);
        return useCase.Execute(new CreatePartnerUseCase
			.Input(input.getCnpj(), input.getEmail(), input.getName()));
	}

	@QueryMapping
    public GetPartnerByIdUseCase.Output partnerOfId(@Argument Long id) {
		final var useCase = new GetPartnerByIdUseCase(partnerService);
    	final var input = new GetPartnerByIdUseCase.Input(id);
        return useCase.Execute(input).orElse(null);
    }
}
