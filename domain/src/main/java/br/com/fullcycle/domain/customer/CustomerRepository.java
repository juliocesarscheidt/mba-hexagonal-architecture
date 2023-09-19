package br.com.fullcycle.domain.customer;

import java.util.Optional;

import br.com.fullcycle.domain.person.CPF;
import br.com.fullcycle.domain.person.Email;

public interface CustomerRepository {

	Optional<Customer> customerOfId(CustomerId id);

	Optional<Customer> customerOfCPF(CPF cpf);

	Optional<Customer> customerOfEmail(Email email);

	Customer create(Customer customer);

	Customer update(Customer customer);
	
	void deleteAll();
}
