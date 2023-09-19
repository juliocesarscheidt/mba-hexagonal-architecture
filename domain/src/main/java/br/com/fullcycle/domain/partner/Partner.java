package br.com.fullcycle.domain.partner;

import java.util.Objects;

import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.person.CNPJ;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.person.Name;

public class Partner {

	private final PartnerId partnerId;
	private Name name;
	private CNPJ cnpj;
	private Email email;
	
	public Partner(final PartnerId partnerId, final String name, final String cnpj, final String email) {
		if (partnerId == null) {
			throw new ValidationException("Invalid partnerId for Partner");
		}
		this.partnerId = partnerId;
		this.setName(name);
		this.setCnpj(cnpj);
		this.setEmail(email);
	}
	
	public static Partner newPartner(final String name, final String cnpj, final String email) {
		return new Partner(PartnerId.unique(), name, cnpj, email);
	}

	public PartnerId getPartnerId() {
		return partnerId;
	}

	public Name getName() {
		return name;
	}

	public CNPJ getCnpj() {
		return cnpj;
	}

	public Email getEmail() {
		return email;
	}
	
	private void setName(String name) {
		this.name = new Name(name);
	}

	private void setCnpj(String cnpj) {
		this.cnpj = new CNPJ(cnpj);
	}

	private void setEmail(String email) {
		this.email = new Email(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Partner other = (Partner) obj;
		return Objects.equals(partnerId, other.getPartnerId());
	}
}
