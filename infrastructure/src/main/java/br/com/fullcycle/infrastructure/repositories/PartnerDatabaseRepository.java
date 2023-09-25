package br.com.fullcycle.infrastructure.repositories;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.person.CNPJ;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.PartnerJpaRepository;

// Interface Adapter
@Component
public class PartnerDatabaseRepository implements PartnerRepository {

	private final PartnerJpaRepository partnerJpaRepository;	
	
	public PartnerDatabaseRepository(PartnerJpaRepository partnerJpaRepository) {
		this.partnerJpaRepository = Objects.requireNonNull(partnerJpaRepository);
	}

	@Override
	public Optional<Partner> partnerOfId(final PartnerId id) {
		return this.partnerJpaRepository
			.findById(UUID.fromString(id.value()))
			.map(PartnerEntity::mapTo);
	}

	@Override
	public Optional<Partner> partnerOfCNPJ(final CNPJ cnpj) {
		return this.partnerJpaRepository
			.findByCnpj(cnpj.value())
			.map(PartnerEntity::mapTo);
	}

	@Override
	public Optional<Partner> partnerOfEmail(final Email email) {
		return this.partnerJpaRepository
			.findByEmail(email.value())
			.map(PartnerEntity::mapTo);
	}

	@Override
	@Transactional
	public Partner create(Partner partner) {
		return this.partnerJpaRepository
			.save(PartnerEntity.mapFrom(partner))
			.mapTo();
	}

	@Override
	public Partner update(Partner partner) {
		return this.partnerJpaRepository
			.save(PartnerEntity.mapFrom(partner))
			.mapTo();
	}

	@Override
	public void deleteAll() {
		this.partnerJpaRepository.deleteAll();
	}
}
