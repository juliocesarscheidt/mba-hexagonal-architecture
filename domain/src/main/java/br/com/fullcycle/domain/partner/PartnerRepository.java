package br.com.fullcycle.domain.partner;

import java.util.Optional;

import br.com.fullcycle.domain.person.CNPJ;
import br.com.fullcycle.domain.person.Email;

public interface PartnerRepository {

	Optional<Partner> partnerOfId(PartnerId id);

	Optional<Partner> partnerOfCNPJ(CNPJ cnpj);

	Optional<Partner> partnerOfEmail(Email email);

	Partner create(Partner partner);
	
	Partner update(Partner partner);
	
	void deleteAll();
}
