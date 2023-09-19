package br.com.fullcycle.domain.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.person.Name;

public class Event {
	
	private static final int ONE = 1;
	private final EventId eventId;
	private Name name;
	private LocalDateTime date;
	private int totalSpots;
	private PartnerId partnerId;
	private Set<EventTicket> tickets;
	
	public Event(
		final EventId eventId,
		final String name,
		final String date,
		final Integer totalSpots,
		final PartnerId partnerId,
		final Set<EventTicket> tickets
	) {
		this(eventId, tickets); // calling the private constructor below
		this.setName(name);
		this.setDate(date);
		this.setTotalSpots(totalSpots);
		this.setPartnerId(partnerId);
	}
	
	private Event(final EventId eventId, final Set<EventTicket> tickets) {
		if (eventId == null) {
			throw new ValidationException("Invalid eventId for Event");
		}
		this.eventId = eventId;
		this.tickets = tickets != null ? tickets : new HashSet<>(0);
	}
	
	public static Event newEvent(final String name,  final String date, final Integer totalSpots, final Partner partner) {
		return new Event(EventId.unique(), name, date, totalSpots, partner.getPartnerId(), null);
	}

	public Ticket reserveTicket(final CustomerId customerId) {
		this.getTickets().stream()
			.filter(it -> Objects.equals(it.getCustomerId(), customerId))
			.findAny()
			.ifPresent(it -> {
				throw new ValidationException("Email already registered");
			});
		
		if (this.getTotalSpots() < this.getTickets().size() + ONE) {
            throw new ValidationException("Event sold out");
        }
		
		final var newTicket = Ticket.newTicket(customerId, getEventId());
		
		this.tickets.add(new EventTicket(newTicket.getTicketId(), eventId, customerId, this.getTickets().size() + 1));
		
		return newTicket;
	}
	
	public EventId getEventId() {
		return eventId;
	}

	public Name getName() {
		return name;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public int getTotalSpots() {
		return totalSpots;
	}

	public PartnerId getPartnerId() {
		return partnerId;
	}
		
	public Set<EventTicket> getTickets() {
		return Collections.unmodifiableSet(tickets);
	}

	private void setName(String name) {
		this.name = new Name(name);
	}
	
	private void setDate(String date) {
		if (date == null) {
			throw new ValidationException("Invalid date for Event");
		}
		try {
			this.date = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		} catch (RuntimeException e) {
			// System.out.println(e.getMessage());
			throw new ValidationException("Invalid date for Event");
		}
	}

	private void setTotalSpots(Integer totalSpots) {
		if (totalSpots == null) {
			throw new ValidationException("Invalid totalSpots for Event");
		}
		this.totalSpots = totalSpots;
	}

	private void setPartnerId(PartnerId partnerId) {
		if (partnerId == null) {
			throw new ValidationException("Invalid partnerId for Event");
		}
		this.partnerId = partnerId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Event other = (Event) obj;
		return Objects.equals(eventId, other.getEventId());
	}

	public static Event restore(
		final String id,
		final String name,
		final String date,
		final int totalSpots,
		final String partnerId,
		final Set<EventTicket> tickets
	) {
		return new Event(
			EventId.with(id),
			name,
			date,
			totalSpots,
			PartnerId.with(partnerId),
			tickets
		);	
	}
}
