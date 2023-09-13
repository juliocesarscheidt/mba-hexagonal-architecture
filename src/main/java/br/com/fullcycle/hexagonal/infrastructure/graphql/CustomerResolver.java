package br.com.fullcycle.hexagonal.infrastructure.graphql;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewCustomerDTO;
import jakarta.xml.bind.ValidationException;

//Adapter
@Controller
public class CustomerResolver {
	
	private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    public CustomerResolver(
		final CreateCustomerUseCase createCustomerUseCase,
		final GetCustomerByIdUseCase getCustomerByIdUseCase
	) {
    	this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
    	this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
	}
    
	@MutationMapping
	public CreateCustomerUseCase.Output createCustomer(@Argument NewCustomerDTO input) throws ValidationException {
        return createCustomerUseCase.Execute(new CreateCustomerUseCase
    		.Input(input.cpf(), input.email(), input.name()));
	}

	@QueryMapping
    public GetCustomerByIdUseCase.Output customerOfId(@Argument Long id) {
    	final var input = new GetCustomerByIdUseCase.Input(id);
        return getCustomerByIdUseCase.Execute(input).orElse(null);
    }
}
