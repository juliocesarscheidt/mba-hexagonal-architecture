package br.com.fullcycle.infrastructure.gateways;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fullcycle.application.ticket.CreateTicketForCustomerUseCase;
import br.com.fullcycle.domain.event.EventTicketReserved;

@Component
public class ConsumerQueueGateway implements QueueGateway {

	private final CreateTicketForCustomerUseCase createTicketForCustomerUseCase;
	private final ObjectMapper mapper;
	private static final Logger LOG = LoggerFactory.getLogger(ConsumerQueueGateway.class);

	public ConsumerQueueGateway(final CreateTicketForCustomerUseCase createTicketForCustomerUseCase, final ObjectMapper mapper) {
		this.createTicketForCustomerUseCase = createTicketForCustomerUseCase;
		this.mapper = mapper;
	}

	@Async(value = "queueExecutor")
	@Override
	public void publish(final String content) {
		if (content == null) return;
		try {
			if (content.contains("event-ticket.reserved")) {
				final var dto = safeRead(content, EventTicketReserved.class);
				this.createTicketForCustomerUseCase.Execute(new CreateTicketForCustomerUseCase.Input(dto.eventTicketId(), dto.eventId(), dto.customerId()));
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	private <T> T safeRead(final String content, Class<T> clazz) {
		try {
			return this.mapper.readValue(content, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
