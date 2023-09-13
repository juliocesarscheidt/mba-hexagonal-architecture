package br.com.fullcycle.hexagonal.infrastructure.graphql;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToTicketUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.hexagonal.infrastructure.dtos.SubscribeDTO;
import jakarta.xml.bind.ValidationException;

//Adapter
@Controller
public class EventResolver {

	private final CreateEventUseCase createEventUseCase;
    private final SubscribeCustomerToTicketUseCase subscribeCustomerToTicketUseCase;

    public EventResolver(
		final CreateEventUseCase createEventUseCase,
		final SubscribeCustomerToTicketUseCase subscribeCustomerToTicketUseCase
	) {
		this.createEventUseCase = Objects.requireNonNull(createEventUseCase);
		this.subscribeCustomerToTicketUseCase = Objects.requireNonNull(subscribeCustomerToTicketUseCase);
	}
    
    @MutationMapping
	public CreateEventUseCase.Output createEvent(@Argument NewEventDTO input) throws ValidationException {
		final var createEventInput = new CreateEventUseCase
			.Input(input.date(), input.name(), input.partnerId(), input.totalSpots());
		return createEventUseCase.Execute(createEventInput);
	}

    @MutationMapping
    public SubscribeCustomerToTicketUseCase.Output subscribeCustomerToTicket(@Argument Long id, @Argument SubscribeDTO dto) {        
        final var input = new SubscribeCustomerToTicketUseCase.Input(dto.customerId(), id);
    	return subscribeCustomerToTicketUseCase.Execute(input);
    }
}
