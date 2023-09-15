package br.com.fullcycle.hexagonal.application.domain.customer;

import java.util.Objects;

import br.com.fullcycle.hexagonal.application.domain.person.CPF;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class Customer {

	private final CustomerId customerId;
	private Name name;
	private CPF cpf;
	private Email email;

	public Customer(final CustomerId customerId, final String name, final String cpf, final String email) {
		if (customerId == null) {
			throw new ValidationException("Invalid customerId for Customer");
		}
		this.customerId = customerId;
		this.setName(name);
		this.setCpf(cpf);
		this.setEmail(email);
	}
	
	public static Customer newCustomer(final String name, final String cpf, final String email) {
		return new Customer(CustomerId.unique(), name, cpf, email);
	}

	public CustomerId getCustomerId() {
		return customerId;
	}

	public Name getName() {
		return name;
	}

	public CPF getCpf() {
		return cpf;
	}

	public Email getEmail() {
		return email;
	}

	private void setName(String name) {
		this.name = new Name(name);
	}

	private void setCpf(String cpf) {
		this.cpf = new CPF(cpf);
	}

	private void setEmail(String email) {
		this.email = new Email(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Customer other = (Customer) obj;
		return Objects.equals(customerId, other.getCustomerId());
	}
}
