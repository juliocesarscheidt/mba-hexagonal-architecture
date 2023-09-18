package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import java.util.UUID;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.EventTicket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import jakarta.persistence.*;

@Entity(name ="EventTicket")
@Table(name = "events_tickets")
public class EventTicketEntity {

	@Id
	private UUID ticketId;
	
	private UUID customerId;
	
	private int ordering;

	@ManyToOne(fetch = FetchType.LAZY)
	private EventEntity event;
	
	public EventTicketEntity() {
	}
	
	public EventTicketEntity(UUID ticketId, UUID customerId, int ordering, EventEntity event) {
		this.ticketId = ticketId;
		this.customerId = customerId;
		this.ordering = ordering;
		this.event = event;
	}
	
	public EventTicket mapTo() {
    	return new EventTicket(
			TicketId.with(this.ticketId.toString()),
			EventId.with(this.event.getId().toString()),
			CustomerId.with(this.customerId.toString()),
			this.ordering
    	);
    }

    public static EventTicketEntity mapFrom(final EventEntity event, final EventTicket eventTicket) {
    	return new EventTicketEntity(
			UUID.fromString(eventTicket.getTicketId().value()),
			UUID.fromString(eventTicket.getCustomerId().value()),
			eventTicket.getOrdering(),
			event
		);
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
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ordering;
		result = prime * result + ((ticketId == null) ? 0 : ticketId.hashCode());
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
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (ordering != other.ordering)
			return false;
		if (ticketId == null) {
			if (other.ticketId != null)
				return false;
		} else if (!ticketId.equals(other.ticketId))
			return false;
		return true;
	}
}
