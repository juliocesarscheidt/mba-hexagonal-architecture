package br.com.fullcycle.hexagonal.infrastructure.graphql;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.partner.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewPartnerDTO;

//Adapter
@Controller
public class PartnerResolver {

	private final CreatePartnerUseCase createPartnerUseCase;
    private final GetPartnerByIdUseCase getPartnerByIdUseCase;

    public PartnerResolver(
		final CreatePartnerUseCase createPartnerUseCase,
		final GetPartnerByIdUseCase getPartnerByIdUseCase
	) {
		this.createPartnerUseCase = Objects.requireNonNull(createPartnerUseCase);
		this.getPartnerByIdUseCase = Objects.requireNonNull(getPartnerByIdUseCase);
	}
	
	@MutationMapping
	public CreatePartnerUseCase.Output createPartner(@Argument NewPartnerDTO input) throws ValidationException {
        return createPartnerUseCase.Execute(new CreatePartnerUseCase
			.Input(input.cnpj(), input.email(), input.name()));
	}

	@QueryMapping
    public GetPartnerByIdUseCase.Output partnerOfId(@Argument String id) {
    	final var input = new GetPartnerByIdUseCase.Input(id);
        return getPartnerByIdUseCase.Execute(input).orElse(null);
    }
}
