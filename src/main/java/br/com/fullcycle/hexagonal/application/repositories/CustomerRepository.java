package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.Customer;
import br.com.fullcycle.hexagonal.application.domain.CustomerId;

public interface CustomerRepository {

	Optional<Customer> customerOfId(CustomerId id);

	Optional<Customer> customerOfCPF(String cpf);

	Optional<Customer> customerOfEmail(String email);

	Customer create(Customer customer);

	Customer update(Customer customer);
	
	void deleteAll();
}