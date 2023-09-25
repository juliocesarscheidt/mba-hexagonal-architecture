package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.infrastructure.dtos.NewPartnerDTO;
import br.com.fullcycle.infrastructure.http.HttpRouter;
import br.com.fullcycle.infrastructure.http.HttpRouter.*;

import java.net.URI;
import java.util.Objects;

//Adapter
public class PartnerFnController {
    
    private final CreatePartnerUseCase createPartnerUseCase;
    private final GetPartnerByIdUseCase getPartnerByIdUseCase;

    public PartnerFnController(
		final CreatePartnerUseCase createPartnerUseCase,
		final GetPartnerByIdUseCase getPartnerByIdUseCase
	) {
		this.createPartnerUseCase = Objects.requireNonNull(createPartnerUseCase);
		this.getPartnerByIdUseCase = Objects.requireNonNull(getPartnerByIdUseCase);
	}
    
    public HttpRouter bind(final HttpRouter router) {
    	router.GET("/partners/{id}", this::get);
    	router.POST("/partners", this::create);
    	return router;
    }

    private HttpResponse<?> create(final HttpRequest req) {
		try {
			final var dto = req.body(NewPartnerDTO.class);
			
			final var input = new CreatePartnerUseCase
				.Input(dto.cnpj(), dto.email(), dto.name());
			final var output = createPartnerUseCase.Execute(input);
			return HttpResponse
				.created(URI.create("/partners/" + output.id()))
				.body(output);

		} catch (ValidationException e) {
			return HttpResponse.unprocessableEntity().body(e.getMessage());
		}
    }

    private HttpResponse<?> get(final HttpRequest req) {
    	final var id = req.pathParam("id");

    	final var input = new GetPartnerByIdUseCase.Input(id);
        final var output = getPartnerByIdUseCase.Execute(input);
		return output
			.map(HttpResponse::ok)
			.orElse(HttpResponse.notFound().build());
    }
}
