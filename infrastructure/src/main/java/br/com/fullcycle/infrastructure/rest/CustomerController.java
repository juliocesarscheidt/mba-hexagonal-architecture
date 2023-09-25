package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.application.customer.CreateCustomerUseCase;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.infrastructure.dtos.NewCustomerDTO;
import br.com.fullcycle.infrastructure.rest.presenters.GetCustomerByIdResponseEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

// Adapter
@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    public CustomerController(
		final CreateCustomerUseCase createCustomerUseCase,
		final GetCustomerByIdUseCase getCustomerByIdUseCase
	) {
    	this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
    	this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
	}

	@PostMapping
    public ResponseEntity<?> create(@RequestBody NewCustomerDTO dto) {
		try {
			final var input = new CreateCustomerUseCase
				.Input(dto.cpf(), dto.email(), dto.name());
			final var output = createCustomerUseCase.Execute(input);
			return ResponseEntity
				.created(URI.create("/customers/" + output.id()))
				.body(output);
		} catch (ValidationException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
    }

    @GetMapping("/{id}")
    public Object get(@PathVariable String id) throws Exception {
    	final var presenter = new GetCustomerByIdResponseEntity();
    	return getCustomerByIdUseCase.Execute(new GetCustomerByIdUseCase.Input(id), presenter);
    }
}