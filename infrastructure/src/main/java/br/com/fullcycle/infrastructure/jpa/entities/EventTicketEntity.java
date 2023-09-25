package br.com.fullcycle.infrastructure.jpa.entities;

import java.util.UUID;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicket;
import br.com.fullcycle.domain.event.EventTicketId;
import br.com.fullcycle.domain.event.ticket.TicketId;
import jakarta.persistence.*;

@Entity(name ="EventTicket")
@Table(name = "events_tickets")
public class EventTicketEntity {

	@Id
	private UUID eventTicketId;
	
	private UUID ticketId;

	private UUID customerId;
	
	private int ordering;

	@ManyToOne(fetch = FetchType.LAZY)
	private EventEntity event;
	
	public EventTicketEntity() {
	}
	
	public EventTicketEntity(UUID eventTicketId, UUID customerId, int ordering, UUID ticketId, EventEntity event) {
		this.eventTicketId = eventTicketId;
		this.customerId = customerId;
		this.ordering = ordering;
		this.ticketId = ticketId;
		this.event = event;
	}
	
	public EventTicket mapTo() {
    	return new EventTicket(
			EventTicketId.with(this.eventTicketId.toString()),
			EventId.with(this.event.getId().toString()),
			CustomerId.with(this.customerId.toString()),
			this.ticketId != null ? TicketId.with(this.ticketId.toString()) : null,
			this.ordering
    	);
    }

    public static EventTicketEntity mapFrom(final EventEntity event, final EventTicket eventTicket) {
    	return new EventTicketEntity(
			UUID.fromString(eventTicket.getEventTicketId().value()),
			UUID.fromString(eventTicket.getCustomerId().value()),
			eventTicket.getOrdering(),
			eventTicket.getTicketId() != null ? UUID.fromString(eventTicket.getTicketId().value()) : null,
			event
		);
    }
    
    public UUID getEventTicketId() {
		return eventTicketId;
	}

	public UUID getTicketId() {
		return ticketId;
	}

	public void setTicketId(UUID ticketId) {
		this.ticketId = ticketId;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	public EventEntity getEvent() {
		return event;
	}

	public void setEvent(EventEntity event) {
		this.event = event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventTicketId == null) ? 0 : eventTicketId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventTicketEntity other = (EventTicketEntity) obj;
		if (eventTicketId == null) {
			if (other.eventTicketId != null)
				return false;
		} else if (!eventTicketId.equals(other.eventTicketId))
			return false;
		return true;
	}
}
