package br.com.fullcycle.infrastructure.repositories;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.infrastructure.jpa.entities.OutboxEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.EventJpaRepository;
import br.com.fullcycle.infrastructure.jpa.repositories.OutboxJpaRepository;

// Interface Adapter
@Component
public class EventDatabaseRepository implements EventRepository {

	private final EventJpaRepository eventJpaRepository;	
	private final OutboxJpaRepository outboxJpaRepository;
	private final ObjectMapper mapper;
	
	public EventDatabaseRepository(
			final EventJpaRepository eventJpaRepository,
			final OutboxJpaRepository outboxJpaRepository,
			final ObjectMapper mapper
	) {
		this.eventJpaRepository = Objects.requireNonNull(eventJpaRepository);
		this.outboxJpaRepository = Objects.requireNonNull(outboxJpaRepository);
		this.mapper = mapper;
	}

	@Override
	public Optional<Event> eventOfId(final EventId id) {
		return this.eventJpaRepository
			.findById(UUID.fromString(id.value()))
			.map(EventEntity::mapTo);
	}
	
	@Override
	@Transactional
	public Event create(Event event) {
		return this.save(event);
	}

	@Override
	@Transactional
	public Event update(Event event) {
		return this.save(event);
	}

	@Override
	public void deleteAll() {
		this.eventJpaRepository.deleteAll();
	}

	public Event save(Event event) {
		this.outboxJpaRepository.saveAll(
			event.getDomainEvents().stream()
				.map(it -> OutboxEntity.mapFrom(it, this::toJson))
				.toList()		
		);
		return this.eventJpaRepository
			.save(EventEntity.mapFrom(event))
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
