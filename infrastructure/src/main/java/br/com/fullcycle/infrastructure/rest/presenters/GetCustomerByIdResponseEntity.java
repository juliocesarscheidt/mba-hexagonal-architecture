package br.com.fullcycle.infrastructure.rest.presenters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import br.com.fullcycle.application.Presenter;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;

public class GetCustomerByIdResponseEntity implements Presenter<Optional<GetCustomerByIdUseCase.Output>, Object> {

	private static final Logger LOG = LoggerFactory.getLogger(GetCustomerByIdResponseEntity.class);
	
	@Override
	public ResponseEntity<?> present(final Optional<GetCustomerByIdUseCase.Output> output) {
		return output.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@Override
	public ResponseEntity<?> present(final Throwable error) {
		LOG.error("An error happened at GetCustomerByIdUseCase", error);
		return ResponseEntity.notFound().build();
	}
}
