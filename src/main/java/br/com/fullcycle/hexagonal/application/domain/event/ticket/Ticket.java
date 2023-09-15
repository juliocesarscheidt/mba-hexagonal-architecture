package br.com.fullcycle.hexagonal.application.domain.event.ticket;

import java.time.Instant;
import java.util.Objects;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class Ticket {

	private final TicketId ticketId;
	private CustomerId customerId;
	private EventId eventId;
	private TicketStatus status;
    private Instant paidAt;
    private Instant reservedAt;
    
	public Ticket(
		final TicketId ticketId, final CustomerId customerId,
		final EventId eventId, final TicketStatus status,
		final Instant paidAt, final Instant reservedAt
	) {
		this.ticketId = ticketId;
		this.setCustomerId(customerId);
		this.setEventId(eventId);
		this.setStatus(status);
		this.setPaidAt(paidAt);
		this.setReservedAt(reservedAt);
	}

	public static Ticket newTicket(
			final CustomerId customerId,
			final EventId eventId
	) {
		return new Ticket(TicketId.unique(), customerId, eventId, TicketStatus.PENDING, null, Instant.now());
	}

	public TicketId getTicketId() {
		return ticketId;
	}
	
	public CustomerId getCustomerId() {
		return customerId;
	}
	public EventId getEventId() {
		return eventId;
	}
	public TicketStatus getStatus() {
		return status;
	}
	public Instant getPaidAt() {
		return paidAt;
	}
	public Instant getReservedAt() {
		return reservedAt;
	}

	private void setCustomerId(CustomerId customerId) {
		if (customerId == null) {
			throw new ValidationException("Invalid customerId for Ticket");
		}
		this.customerId = customerId;
	}
	private void setEventId(EventId eventId) {
		if (eventId == null) {
			throw new ValidationException("Invalid eventId for Ticket");
		}
		this.eventId = eventId;
	}
	private void setStatus(TicketStatus status) {
		if (status == null) {
			throw new ValidationException("Invalid status for Ticket");
		}
		this.status = status;
	}
	private void setPaidAt(Instant paidAt) {
		this.paidAt = paidAt;
	}
	private void setReservedAt(Instant reservedAt) {
		if (reservedAt == null) {
			throw new ValidationException("Invalid reservedAt for Ticket");
		}
		this.reservedAt = reservedAt;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Ticket other = (Ticket) obj;
		return Objects.equals(ticketId, other.getTicketId());
	}
}
