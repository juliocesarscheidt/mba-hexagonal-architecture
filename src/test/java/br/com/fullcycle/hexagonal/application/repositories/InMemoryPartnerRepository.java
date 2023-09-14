package br.com.fullcycle.hexagonal.application.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.Partner;
import br.com.fullcycle.hexagonal.application.domain.PartnerId;

public class InMemoryPartnerRepository implements PartnerRepository {
	
	private final Map<String, Partner> partners;
	private final Map<String, Partner> partnersByCNPJ;
	private final Map<String, Partner> partnersByEmail;

	public InMemoryPartnerRepository() {
		this.partners = new HashMap<>();
		this.partnersByCNPJ = new HashMap<>();
		this.partnersByEmail = new HashMap<>();
	}

	@Override
	public Optional<Partner> partnerOfId(PartnerId id) {
		return Optional.ofNullable(this.partners.get(Objects.requireNonNull(id).value().toString()));
	}

	@Override
	public Optional<Partner> partnerOfCNPJ(String cnpj) {
		return Optional.ofNullable(this.partnersByCNPJ.get(Objects.requireNonNull(cnpj)));
	}

	@Override
	public Optional<Partner> partnerOfEmail(String email) {
		return Optional.ofNullable(this.partnersByEmail.get(Objects.requireNonNull(email)));
	}

	@Override
	public Partner create(Partner partner) {
		this.partners.put(partner.getPartnerId().value().toString(), partner);
		this.partnersByCNPJ.put(partner.getCnpj().value(), partner);
		this.partnersByEmail.put(partner.getEmail().value(), partner);
		return partner;
	}

	@Override
	public Partner update(Partner partner) {
		this.partners.put(partner.getPartnerId().value().toString(), partner);
		this.partnersByCNPJ.put(partner.getCnpj().value(), partner);
		this.partnersByEmail.put(partner.getEmail().value(), partner);
		return partner;
	}
	
	@Override
    public void deleteAll() {
        this.partners.clear();
        this.partnersByCNPJ.clear();
        this.partnersByEmail.clear();
    }
}
