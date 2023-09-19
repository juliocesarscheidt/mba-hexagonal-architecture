package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventTicket;

@Entity(name = "Event")
@Table(name = "events")
public class EventEntity {

    @Id
    private UUID id;

    private String name;

    private LocalDateTime date;

    private int totalSpots;

    private UUID partnerId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch = FetchType.EAGER)
    private Set<EventTicketEntity> tickets;

    public EventEntity() {
        this.tickets = new HashSet<>();
    }

    public EventEntity(UUID id, String name, LocalDateTime date, int totalSpots, UUID partnerId) {
    	this();
        this.id = id;
        this.name = name;
        this.date = date;
        this.totalSpots = totalSpots;
        this.partnerId = partnerId;
    }
    
    public Event mapTo() {
    	return Event.restore(
    		this.getId().toString(),
    		this.getName(),
    		this.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    		this.getTotalSpots(),
    		this.getPartnerId().toString(),
    		this.getTickets().stream()
	    		.map(EventTicketEntity::mapTo)
	    		.collect(Collectors.toSet())
		);
    }

    public static EventEntity mapFrom(final Event event) {
    	final var entity = new EventEntity(
    		UUID.fromString(event.getEventId().value()),
    		event.getName().value(),
    		event.getDate(),
    		event.getTotalSpots(),
    		UUID.fromString(event.getPartnerId().value())
		);
    	event.getTickets().forEach(entity::addTicket);

    	return entity;
    }
    
    private void addTicket(final EventTicket ticket) {
    	this.tickets.add(EventTicketEntity.mapFrom(this, ticket));
    }

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public int getTotalSpots() {
		return totalSpots;
	}

	public UUID getPartnerId() {
		return partnerId;
	}

	public Set<EventTicketEntity> getTickets() {
		return tickets;
	}
}
