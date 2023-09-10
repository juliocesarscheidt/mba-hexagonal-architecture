package br.com.fullcycle.hexagonal.controllers;

import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.dtos.CustomerDTO;
import br.com.fullcycle.hexagonal.services.CustomerService;
import jakarta.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

// Adapter
@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerDTO dto) {
    	final var useCase = new CreateCustomerUseCase(customerService);
		try {
			final var input = new CreateCustomerUseCase
				.Input(dto.getCpf(), dto.getEmail(), dto.getName());
			final var output = useCase.Execute(input);
			return ResponseEntity
				.created(URI.create("/customers/" + output.id()))
				.body(output);
		} catch (ValidationException e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
    	final var useCase = new GetCustomerByIdUseCase(customerService);
    	final var input = new GetCustomerByIdUseCase.Input(id);
        final var output = useCase.Execute(input);
		return output.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
    }
}