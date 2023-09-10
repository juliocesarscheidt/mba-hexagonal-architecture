package br.com.fullcycle.hexagonal.controllers;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToTicketUseCase;
import br.com.fullcycle.hexagonal.dtos.EventDTO;
import br.com.fullcycle.hexagonal.dtos.SubscribeDTO;
import br.com.fullcycle.hexagonal.services.CustomerService;
import br.com.fullcycle.hexagonal.services.EventService;
import br.com.fullcycle.hexagonal.services.PartnerService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EventService eventService;

    @Autowired
    private PartnerService partnerService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<?> create(@RequestBody EventDTO dto) {
		final var useCase = new CreateEventUseCase(eventService, partnerService);
		try {
			final var partnerId = Objects.requireNonNull(dto.getPartner(), "Partner not found").getId();
			final var input = new CreateEventUseCase
				.Input(dto.getDate(), dto.getName(), partnerId, dto.getTotalSpots());
			final var output = useCase.Execute(input);
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
        	final var useCase = new SubscribeCustomerToTicketUseCase(eventService, customerService);
        	final var input = new SubscribeCustomerToTicketUseCase.Input(id, dto.getCustomerId());
        	final var output = useCase.Execute(input);
        	return ResponseEntity.ok(output);
    	} catch (ValidationException e) {
    		return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
    }
}
