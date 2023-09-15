package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.event.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.event.SubscribeCustomerToEventUseCase;
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
    private final SubscribeCustomerToEventUseCase subscribeCustomerToTicketUseCase;

    public EventController(
		final CreateEventUseCase createEventUseCase,
		final SubscribeCustomerToEventUseCase subscribeCustomerToTicketUseCase
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
    public ResponseEntity<?> subscribe(@PathVariable String id, @RequestBody SubscribeDTO dto) {
    	try {
        	final var input = new SubscribeCustomerToEventUseCase.Input(dto.customerId(), id);
        	final var output = subscribeCustomerToTicketUseCase.Execute(input);
        	return ResponseEntity.ok(output);
    	} catch (ValidationException e) {
    		return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
    }
}
