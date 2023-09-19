package br.com.fullcycle.application.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.person.CPF;
import br.com.fullcycle.domain.person.Email;

public class InMemoryCustomerRepository implements CustomerRepository {
	
	private final Map<String, Customer> customers;
	private final Map<String, Customer> customersByCPF;
	private final Map<String, Customer> customersByEmail;

	public InMemoryCustomerRepository() {
		this.customers = new HashMap<>();
		this.customersByCPF = new HashMap<>();
		this.customersByEmail = new HashMap<>();
	}

	@Override
	public Optional<Customer> customerOfId(CustomerId id) {
		return Optional.ofNullable(this.customers.get(Objects.requireNonNull(id).value()));
	}

	@Override
	public Optional<Customer> customerOfCPF(CPF cpf) {
		return Optional.ofNullable(this.customersByCPF.get(Objects.requireNonNull(cpf).value()));
	}

	@Override
	public Optional<Customer> customerOfEmail(Email email) {
		return Optional.ofNullable(this.customersByEmail.get(Objects.requireNonNull(email).value()));
	}

	@Override
	public Customer create(Customer customer) {
		this.customers.put(customer.getCustomerId().value().toString(), customer);
		this.customersByCPF.put(customer.getCpf().value(), customer);
		this.customersByEmail.put(customer.getEmail().value(), customer);
		return customer;
	}

	@Override
	public Customer update(Customer customer) {
		this.customers.put(customer.getCustomerId().value().toString(), customer);
		this.customersByCPF.put(customer.getCpf().value(), customer);
		this.customersByEmail.put(customer.getEmail().value(), customer);
		return customer;
	}

	@Override
    public void deleteAll() {
        this.customers.clear();
        this.customersByCPF.clear();
        this.customersByEmail.clear();
    }
}
