package br.com.fullcycle.hexagonal.infrastructure.controllers;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.PartnerDTO;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

//Adapter
@RestController
@RequestMapping(value = "partners")
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PartnerDTO dto) {
    	final var useCase = new CreatePartnerUseCase(partnerService);
		try {
			final var input = new CreatePartnerUseCase
				.Input(dto.getCnpj(), dto.getEmail(), dto.getName());
			final var output = useCase.Execute(input);
			return ResponseEntity
				.created(URI.create("/partners/" + output.id()))
				.body(output);
		} catch (ValidationException e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
    	final var useCase = new GetPartnerByIdUseCase(partnerService);
    	final var input = new GetPartnerByIdUseCase.Input(id);
        final var output = useCase.Execute(input);
		return output.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
    }
}
