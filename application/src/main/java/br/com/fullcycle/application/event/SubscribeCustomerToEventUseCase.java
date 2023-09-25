package br.com.fullcycle.application.event;

import java.time.Instant;
import java.util.Objects;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.event.EventTicket;

public class SubscribeCustomerToEventUseCase extends UseCase<SubscribeCustomerToEventUseCase.Input, SubscribeCustomerToEventUseCase.Output> {

	public record Input(String customerId, String eventId) {}
	
	public record Output(String eventId, String eventTicketId, Instant reservationDate) {}
	
	private final CustomerRepository customerRepository;
	private final EventRepository eventRepository;

	public SubscribeCustomerToEventUseCase(
			final CustomerRepository customerRepository,
			final EventRepository eventRepository
		) {
		this.customerRepository = Objects.requireNonNull(customerRepository);
		this.eventRepository = Objects.requireNonNull(eventRepository);
	}
	
	@Override
	public Output Execute(final Input input) throws ValidationException {
		var customer = customerRepository.customerOfId(CustomerId.with(input.customerId))
			.orElseThrow(() -> new ValidationException("Customer not found"));
  
        var event = eventRepository.eventOfId(EventId.with(input.eventId))
    		.orElseThrow(() -> new ValidationException("Event not found"));
        
        final EventTicket ticket = event.reserveTicket(customer.getCustomerId());

        // ticketRepository.create(ticket);
        eventRepository.update(event);

        return new Output(
    		event.getEventId().value(),
    		ticket.getEventTicketId().value(),
    		Instant.now()
    	);
	}
}
