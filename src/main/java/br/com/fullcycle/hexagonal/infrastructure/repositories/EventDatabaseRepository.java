package br.com.fullcycle.hexagonal.infrastructure.repositories;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.EventJpaRepository;

// Interface Adapter
@Component
public class EventDatabaseRepository implements EventRepository {

	private final EventJpaRepository eventJpaRepository;	
	
	public EventDatabaseRepository(EventJpaRepository eventJpaRepository) {
		this.eventJpaRepository = Objects.requireNonNull(eventJpaRepository);
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
		return this.eventJpaRepository
			.save(EventEntity.mapFrom(event))
			.mapTo();
	}

	@Override
	public Event update(Event event) {
		return this.eventJpaRepository
			.save(EventEntity.mapFrom(event))
			.mapTo();
	}

	@Override
	public void deleteAll() {
		this.eventJpaRepository.deleteAll();
	}
}
