package br.com.fullcycle.hexagonal.infrastructure.controllers;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToTicketUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.hexagonal.infrastructure.dtos.SubscribeDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;

//Adapter
@RestController
@RequestMapping(value = "events")
public class EventController {

	private final CreateEventUseCase createEventUseCase;
    private final SubscribeCustomerToTicketUseCase subscribeCustomerToTicketUseCase;

    public EventController(
		final CreateEventUseCase createEventUseCase,
		final SubscribeCustomerToTicketUseCase subscribeCustomerToTicketUseCase
	) {
		this.createEventUseCase = Objects.requireNonNull(createEventUseCase);
		this.subscribeCustomerToTicketUseCase = Objects.requireNonNull(subscribeCustomerToTicketUseCase);
	}

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<?> create(@RequestBody NewEventDTO dto) {
		try {
			final var input = new CreateEventUseCase
				.Input(dto.date(), dto.name(), dto.partnerId(), dto.totalSpots());
			final var output = createEventUseCase.Execute(input);
			return ResponseEntity
				.created(URI.create("/events/" + output.id()))
				.body(output);
    	} catch (ValidationException e) {
    		return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
    }

    @Transactional
    @PostMapping(value = "/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable Long id, @RequestBody SubscribeDTO dto) {
    	try {
        	final var input = new SubscribeCustomerToTicketUseCase.Input(dto.customerId(), id);
        	final var output = subscribeCustomerToTicketUseCase.Execute(input);
        	return ResponseEntity.ok(output);
    	} catch (ValidationException e) {
    		return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
    }
}
