package br.com.fullcycle.hexagonal.infrastructure.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketEntity;

import java.util.UUID;

public interface TicketJpaRepository extends CrudRepository<TicketEntity, UUID> {

    // Optional<TicketEntity> findByEventIdAndCustomerId(Long id, Long customerId);
}
