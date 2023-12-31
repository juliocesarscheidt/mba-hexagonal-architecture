package br.com.fullcycle.hexagonal.infrastructure.repositories;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.TicketJpaRepository;

// Interface Adapter
@Component
public class TicketDatabaseRepository implements TicketRepository {

	private final TicketJpaRepository ticketJpaRepository;	
	
	public TicketDatabaseRepository(final TicketJpaRepository ticketJpaRepository) {
        this.ticketJpaRepository = Objects.requireNonNull(ticketJpaRepository);
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
        return this.ticketJpaRepository.save(TicketEntity.mapFrom(ticket))
            .mapTo();
    }

    @Override
    @Transactional
    public Ticket update(Ticket ticket) {
        return this.ticketJpaRepository.save(TicketEntity.mapFrom(ticket))
            .mapTo();
    }

    @Override
    public void deleteAll() {
        this.ticketJpaRepository.deleteAll();
    }
}
