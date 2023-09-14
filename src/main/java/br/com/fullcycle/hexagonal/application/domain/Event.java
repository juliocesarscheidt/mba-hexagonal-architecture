package br.com.fullcycle.hexagonal.application.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class Event {
	
	private static final int ONE = 1;
	private final EventId eventId;
	private Name name;
	private LocalDate date;
	private int totalSpots;
	private PartnerId partnerId;
	private Set<EventTicket> tickets;
	
	public Event(final EventId eventId, final String name, final String date, final Integer totalSpots, final PartnerId partnerId) {
		this(eventId); // calling the private constructor below
		this.setName(name);
		this.setDate(date);
		this.setTotalSpots(totalSpots);
		this.setPartnerId(partnerId);
	}
	
	private Event(final EventId eventId) {
		if (eventId == null) {
			throw new ValidationException("Invalid eventId for Event");
		}
		this.eventId = eventId;
		this.tickets = new HashSet<>(0);
	}
	
	public static Event newEvent(final String name,  final String date, final Integer totalSpots, final Partner partner) {
		return new Event(EventId.unique(), name, date, totalSpots, partner.getPartnerId());
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

	public LocalDate getDate() {
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
		this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
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
}
