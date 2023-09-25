package br.com.fullcycle.infrastructure.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.fullcycle.infrastructure.jpa.entities.PartnerEntity;

import java.util.Optional;
import java.util.UUID;

public interface PartnerJpaRepository extends CrudRepository<PartnerEntity, UUID> {

    Optional<PartnerEntity> findByCnpj(String cnpj);

    Optional<PartnerEntity> findByEmail(String email);
}