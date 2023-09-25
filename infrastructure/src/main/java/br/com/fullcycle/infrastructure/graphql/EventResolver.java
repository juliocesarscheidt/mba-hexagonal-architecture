package br.com.fullcycle.infrastructure.graphql;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullcycle.application.event.CreateEventUseCase;
import br.com.fullcycle.application.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.infrastructure.dtos.SubscribeDTO;
import jakarta.xml.bind.ValidationException;

//Adapter
@Controller
public class EventResolver {

	private final CreateEventUseCase createEventUseCase;
    private final SubscribeCustomerToEventUseCase subscribeCustomerToTicketUseCase;

    public EventResolver(
		final CreateEventUseCase createEventUseCase,
		final SubscribeCustomerToEventUseCase subscribeCustomerToTicketUseCase
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

    @Transactional
    @MutationMapping
    public SubscribeCustomerToEventUseCase.Output subscribeCustomerToTicket(@Argument SubscribeDTO input) {        
        final var subscribeCustomerToTicketInput = new SubscribeCustomerToEventUseCase.Input(
        		input.customerId(), input.eventId());
    	return subscribeCustomerToTicketUseCase.Execute(subscribeCustomerToTicketInput);
    }
}
