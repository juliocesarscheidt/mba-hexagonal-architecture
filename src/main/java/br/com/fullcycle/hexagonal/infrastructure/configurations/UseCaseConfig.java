package br.com.fullcycle.hexagonal.infrastructure.configurations;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToTicketUseCase;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;

@Configuration
public class UseCaseConfig {

	private final CustomerService customerService;
	private final EventService eventService;
	private final PartnerService partnerService;

	public UseCaseConfig(
		final CustomerService customerService,
		final EventService eventService,
		final PartnerService partnerService
	) {
		this.customerService = Objects.requireNonNull(customerService);
		this.eventService = Objects.requireNonNull(eventService);
		this.partnerService = Objects.requireNonNull(partnerService);
	}
	
	@Bean
	public CreateCustomerUseCase createCustomerUseCase() {
		return new CreateCustomerUseCase(this.customerService);
	}
	
	@Bean
	public CreateEventUseCase createEventUseCase() {
		return new CreateEventUseCase(eventService, partnerService);
	}
	
	@Bean
	public CreatePartnerUseCase createPartnerUseCase() {
		return new CreatePartnerUseCase(partnerService);
	}

	@Bean
	public GetCustomerByIdUseCase getCustomerByIdUseCase() {
		return new GetCustomerByIdUseCase(customerService);
	}

	@Bean
	public GetPartnerByIdUseCase getPartnerByIdUseCase() {
		return new GetPartnerByIdUseCase(partnerService);
	}
	
	@Bean
	public SubscribeCustomerToTicketUseCase subscribeCustomerToTicketUseCase() {
		return new SubscribeCustomerToTicketUseCase(eventService, customerService);
	}
}