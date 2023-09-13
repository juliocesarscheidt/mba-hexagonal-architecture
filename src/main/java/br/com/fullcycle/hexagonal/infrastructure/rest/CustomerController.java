package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewCustomerDTO;

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
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
    	final var input = new GetCustomerByIdUseCase.Input(id);
        final var output = getCustomerByIdUseCase.Execute(input);
		return output.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
    }
}