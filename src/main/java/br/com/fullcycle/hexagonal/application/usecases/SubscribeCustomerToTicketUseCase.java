package br.com.fullcycle.hexagonal.application.usecases;

import java.time.Instant;
import java.util.Objects;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Ticket;
import br.com.fullcycle.hexagonal.models.TicketStatus;
import br.com.fullcycle.hexagonal.services.CustomerService;
import br.com.fullcycle.hexagonal.services.EventService;

public class SubscribeCustomerToTicketUseCase extends UseCase<SubscribeCustomerToTicketUseCase.Input, SubscribeCustomerToTicketUseCase.Output> {

	public record Input(Long eventId, Long customerId) {}
	
	public record Output(Long eventId, String ticketStatus, Instant reservationData) {}
	
	private final EventService eventService;
	private final CustomerService customerService;

	public SubscribeCustomerToTicketUseCase(final EventService eventService, final CustomerService customerService) {
		this.eventService = Objects.requireNonNull(eventService);
		this.customerService = Objects.requireNonNull(customerService);
	}
	
	@Override
	public Output Execute(final Input input) throws ValidationException {
		var customer = customerService.findById(input.customerId)
			.orElseThrow(() -> new ValidationException("Customer not found"));
  
        var event = eventService.findById(input.eventId)
    		.orElseThrow(() -> new ValidationException("Event not found"));

        eventService.findTicketByEventIdAndCustomerId(input.eventId, input.customerId)
        	.ifPresent(t -> {
        		throw new ValidationException("Email already registered");
        	});

        if (event.getTotalSpots() < event.getTickets().size() + 1) {
            throw new ValidationException("Event sold out");
        }

        var ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setCustomer(customer);
        ticket.setReservedAt(Instant.now());
        ticket.setStatus(TicketStatus.PENDING);

        event.getTickets().add(ticket);

        eventService.save(event);

        return new Output(event.getId(), ticket.getStatus().name(), ticket.getReservedAt());
	}
}