package br.com.fullcycle.infrastructure.repositories;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.person.CPF;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.CustomerJpaRepository;

// Interface Adapter
@Component
public class CustomerDatabaseRepository implements CustomerRepository {

	private final CustomerJpaRepository customerJpaRepository;	
	
	public CustomerDatabaseRepository(CustomerJpaRepository customerJpaRepository) {
		this.customerJpaRepository = Objects.requireNonNull(customerJpaRepository);
	}

	@Override
	public Optional<Customer> customerOfId(final CustomerId id) {
		return this.customerJpaRepository
			.findById(UUID.fromString(id.value()))
			.map(CustomerEntity::mapTo);
	}

	@Override
	public Optional<Customer> customerOfCPF(final CPF cpf) {
		return this.customerJpaRepository
			.findByCpf(cpf.value())
			.map(CustomerEntity::mapTo);
	}

	@Override
	public Optional<Customer> customerOfEmail(final Email email) {
		return this.customerJpaRepository
			.findByEmail(email.value())
			.map(CustomerEntity::mapTo);
	}

	@Override
	@Transactional
	public Customer create(Customer customer) {
		return this.customerJpaRepository
			.save(CustomerEntity.mapFrom(customer))
			.mapTo();
	}

	@Override
	public Customer update(Customer customer) {
		return this.customerJpaRepository
			.save(CustomerEntity.mapFrom(customer))
			.mapTo();
	}

	@Override
	public void deleteAll() {
		this.customerJpaRepository.deleteAll();
	}
}
