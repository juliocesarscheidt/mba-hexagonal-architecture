package br.com.fullcycle.infrastructure.repositories;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.event.ticket.TicketId;
import br.com.fullcycle.domain.event.ticket.TicketRepository;
import br.com.fullcycle.infrastructure.jpa.entities.OutboxEntity;
import br.com.fullcycle.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.OutboxJpaRepository;
import br.com.fullcycle.infrastructure.jpa.repositories.TicketJpaRepository;

// Interface Adapter
@Component
public class TicketDatabaseRepository implements TicketRepository {

	private final TicketJpaRepository ticketJpaRepository;
	private final OutboxJpaRepository outboxJpaRepository;
	private final ObjectMapper mapper;
	
	public TicketDatabaseRepository(
		final TicketJpaRepository ticketJpaRepository,
		final OutboxJpaRepository outboxJpaRepository,
		final ObjectMapper mapper
	) {
        this.ticketJpaRepository = Objects.requireNonNull(ticketJpaRepository);
        this.outboxJpaRepository = Objects.requireNonNull(outboxJpaRepository);
		this.mapper = mapper;
    }

    @Override
    public Optional<Ticket> ticketOfId(final TicketId anId) {
        Objects.requireNonNull(anId, "id cannot be null");
        return this.ticketJpaRepository.findById(UUID.fromString(anId.value()))
            .map(TicketEntity::mapTo);
    }

    @Override
    @Transactional
    public Ticket create(final Ticket ticket) {
    	return this.save(ticket);
    }

    @Override
    @Transactional
    public Ticket update(Ticket ticket) {
    	return this.save(ticket);
    }

    @Override
    public void deleteAll() {
        this.ticketJpaRepository.deleteAll();
    }
    
    public Ticket save(final Ticket ticket) {
    	this.outboxJpaRepository.saveAll(
			ticket.getDomainEvents().stream()
				.map(it -> OutboxEntity.mapFrom(it, this::toJson))
				.toList()		
		);
        return this.ticketJpaRepository.save(TicketEntity.mapFrom(ticket))
            .mapTo();
	}
    
    private String toJson(DomainEvent domainEvent) {
		try {
			return this.mapper.writeValueAsString(domainEvent);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
