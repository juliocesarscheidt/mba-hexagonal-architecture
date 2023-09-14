package br.com.fullcycle.hexagonal.application.usecases;

import java.time.Instant;
import java.util.Objects;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.domain.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.EventId;
import br.com.fullcycle.hexagonal.application.domain.Ticket;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;

public class SubscribeCustomerToEventUseCase extends UseCase<SubscribeCustomerToEventUseCase.Input, SubscribeCustomerToEventUseCase.Output> {

	public record Input(String customerId, String eventId) {}
	
	public record Output(String eventId, String ticketId, String ticketStatus, Instant reservationDate) {}
	
	private final CustomerRepository customerRepository;
	private final EventRepository eventRepository;
	private final TicketRepository ticketRepository;

	public SubscribeCustomerToEventUseCase(
			final CustomerRepository customerRepository,
			final EventRepository eventRepository,
			final TicketRepository ticketRepository
		) {
		this.customerRepository = Objects.requireNonNull(customerRepository);
		this.eventRepository = Objects.requireNonNull(eventRepository);
		this.ticketRepository = Objects.requireNonNull(ticketRepository);
	}
	
	@Override
	public Output Execute(final Input input) throws ValidationException {
		var customer = customerRepository.customerOfId(CustomerId.with(input.customerId))
			.orElseThrow(() -> new ValidationException("Customer not found"));
  
        var event = eventRepository.eventOfId(EventId.with(input.eventId))
    		.orElseThrow(() -> new ValidationException("Event not found"));
        
        final Ticket ticket = event.reserveTicket(customer.getCustomerId());

        ticketRepository.create(ticket);
        eventRepository.update(event);

        return new Output(
    		event.getEventId().value(),
    		ticket.getTicketId().value(),
    		ticket.getStatus().name(),
    		ticket.getReservedAt()
    	);
	}
}
