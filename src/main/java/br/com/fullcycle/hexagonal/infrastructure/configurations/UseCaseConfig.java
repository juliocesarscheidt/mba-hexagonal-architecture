package br.com.fullcycle.hexagonal.infrastructure.configurations;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import br.com.fullcycle.hexagonal.application.usecases.customer.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.event.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.GetPartnerByIdUseCase;

@Configuration
public class UseCaseConfig {

	private final CustomerRepository customerRepository;
	private final PartnerRepository partnerRepository;
	private final EventRepository eventRepository;
	private final TicketRepository ticketRepository;

	public UseCaseConfig(
		final CustomerRepository customerRepository,
		final PartnerRepository partnerRepository,
		final EventRepository eventRepository,
		final TicketRepository ticketRepository
	) {
		this.customerRepository = Objects.requireNonNull(customerRepository);
		this.partnerRepository = Objects.requireNonNull(partnerRepository);;
		this.eventRepository = Objects.requireNonNull(eventRepository);;
		this.ticketRepository = Objects.requireNonNull(ticketRepository);;
	}

	@Bean
	public CreateCustomerUseCase createCustomerUseCase() {
		return new CreateCustomerUseCase(customerRepository);
	}
	
	@Bean
	public GetCustomerByIdUseCase getCustomerByIdUseCase() {
		return new GetCustomerByIdUseCase(customerRepository);
	}

	@Bean
	public CreatePartnerUseCase createPartnerUseCase() {
		return new CreatePartnerUseCase(partnerRepository);
	}

	@Bean
	public GetPartnerByIdUseCase getPartnerByIdUseCase() {
		return new GetPartnerByIdUseCase(partnerRepository);
	}

	@Bean
	public CreateEventUseCase createEventUseCase() {
		return new CreateEventUseCase(eventRepository, partnerRepository);
	}

	@Bean
	public SubscribeCustomerToEventUseCase subscribeCustomerToTicketUseCase() {
		return new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);
	}
}
