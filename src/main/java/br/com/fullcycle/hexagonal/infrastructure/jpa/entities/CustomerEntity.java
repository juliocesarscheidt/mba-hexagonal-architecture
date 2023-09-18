package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;

@Entity(name = "Customer")
@Table(name = "customers")
public class CustomerEntity {

    @Id
    private UUID id;

    private String name;

    private String cpf;

    private String email;

    public CustomerEntity() {
    }

    public CustomerEntity(UUID id, String name, String cpf, String email) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
    }

    public Customer mapTo() {
    	return new Customer(
			CustomerId.with(this.id.toString()),
			this.name,
			this.cpf,
			this.email
		);
    }

    public static CustomerEntity mapFrom(final Customer customer) {
    	return new CustomerEntity(
			UUID.fromString(customer.getCustomerId().value()),
			customer.getName().value(),
			customer.getCpf().value(),
			customer.getEmail().value()
		);
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity customer = (CustomerEntity) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
