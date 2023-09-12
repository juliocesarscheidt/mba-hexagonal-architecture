package br.com.fullcycle.hexagonal.application.usecases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infrastructure.repositories.TicketRepository;
import io.hypersistence.tsid.TSID;

public class SubscribeCustomerToTicketUseCaseIT extends IntegrationTest {

	@Autowired
	private SubscribeCustomerToTicketUseCase useCase;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@BeforeEach
	void tearDown() {
		eventRepository.deleteAll();
		customerRepository.deleteAll();
		ticketRepository.deleteAll();
	}
	
	private Event createEvent(String name, Integer totalSpots) {
		final var aEvent = new Event();
    	aEvent.setName(name);
    	aEvent.setTotalSpots(totalSpots);

    	return eventRepository.save(aEvent);
	}
	
	private Customer createCustomer(final String cpf, final String email, final String name) {
    	final var aCustomer = new Customer();
    	// aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
    	aCustomer.setCpf(cpf);
    	aCustomer.setEmail(email);
    	aCustomer.setName(name);

    	return customerRepository.save(aCustomer);
    }
	
	@Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() throws Exception {
		// tests steps:
    	// - given
		// final var expectedTicketSize = 1;
		final var aCustomer = createCustomer("12345678901", "john.doe@gmail.com", "John Doe");
		final var customerId = aCustomer.getId();
		System.out.println(aCustomer);
    	
    	final var aEvent = createEvent("Disney", 10);
    	final var eventId = aEvent.getId();
    	System.out.println(aEvent);

    	final var subscribeInput =
			new SubscribeCustomerToTicketUseCase.Input(customerId, eventId);

    	// - when
    	/*
    	final var eventService = mock(EventService.class);
    	final var customerService = mock(CustomerService.class);
    	
    	when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
    	when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
    	when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.empty());
    	when(eventService.save(any())).thenAnswer(a -> {
    		final var event = a.getArgument(0, Event.class);
    		Assertions.assertEquals(expectedTicketSize, event.getTickets().size());
    		return event;
    	});
    	*/

    	final var output = useCase.Execute(subscribeInput);
    	// System.out.println(output);
    	// Output[eventId=488902434593655822, ticketStatus=PENDING, reservationData=2023-09-11T02:43:45.220043200Z]
    	
    	// - then
    	Assertions.assertNotNull(output.eventId());
    	Assertions.assertNotNull(output.reservationData());
    	Assertions.assertEquals(eventId, output.eventId());
    	Assertions.assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());
    }

	@Test
    @DisplayName("Nao deve comprar um ticket de um evento se o evento nao existir")
    public void testReserveTicketWhenEventDoesntExist() throws Exception {
		// tests steps:
    	// - given
		final var expectedError = "Event not found";
		
		final var aCustomer = createCustomer("12345678901", "john.doe@gmail.com", "John Doe");
		final var customerId = aCustomer.getId();
    	
    	final var eventId = TSID.fast().toLong();
    
    	final var subscribeInput =
			new SubscribeCustomerToTicketUseCase.Input(customerId, eventId);

    	// - when
    	/*
    	final var eventService = mock(EventService.class);
    	final var customerService = mock(CustomerService.class);
    	
    	when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
    	when(eventService.findById(eventId)).thenReturn(Optional.empty());
    	*/

    	final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
    		useCase.Execute(subscribeInput);
    	});

    	// - then
    	Assertions.assertEquals(expectedError, actualException.getMessage());
    }

	@Test
    @DisplayName("Nao deve comprar um ticket de um evento se o cliente nao existir")
    public void testReserveTicketWhenCustomerDoesntExist() throws Exception {
		// tests steps:
    	// - given
		final var expectedError = "Customer not found";
		
		final var customerId = TSID.fast().toLong();
    	
    	final var eventId = TSID.fast().toLong();
    
    	final var subscribeInput =
			new SubscribeCustomerToTicketUseCase.Input(customerId, eventId);

    	// - when
    	/*
    	final var eventService = mock(EventService.class);
    	final var customerService = mock(CustomerService.class);
    	
    	when(customerService.findById(customerId)).thenReturn(Optional.empty());
    	*/

    	final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
    		useCase.Execute(subscribeInput);
    	});

    	// - then
    	Assertions.assertEquals(expectedError, actualException.getMessage());
    }
	
	@Test
    @DisplayName("Um mesmo cliente nao pode comprar mais de um ticket por event")
    public void testReserveTicketWhenCustomerAlreadySubscribedToEvent() throws Exception {
		// tests steps:
    	// - given
		final var expectedError = "Email already registered";

		final var aCustomer = createCustomer("12345678901", "john.doe@gmail.com", "John Doe");
		final var customerId = aCustomer.getId();
    	
		final var aEvent = createEvent("Disney", 10);
    	final var eventId = aEvent.getId();

    	final var subscribeInput =
			new SubscribeCustomerToTicketUseCase.Input(customerId, eventId);
    	
    	// - when
    	/*
    	final var eventService = mock(EventService.class);
    	final var customerService = mock(CustomerService.class);
    	
    	when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
    	when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
    	when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.of(new Ticket()));
    	*/

    	useCase.Execute(subscribeInput);
    	
    	final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
    		useCase.Execute(subscribeInput);
    	});
	
    	// - then
    	Assertions.assertEquals(expectedError, actualException.getMessage());
    }
	
	@Test
	@DisplayName("Nao deve comprar um ticket de um evento se o evento ja esgotou")
    public void testReserveTicketWhenEventSoldOut() throws Exception {
		// tests steps:
    	// - given
		final var expectedError = "Event sold out";
		
		final var aCustomer = createCustomer("12345678901", "john.doe@gmail.com", "John Doe");
		final var customerId = aCustomer.getId();
    	
		final var aEvent = createEvent("Disney", 0);
    	final var eventId = aEvent.getId();

    	final var subscribeInput =
			new SubscribeCustomerToTicketUseCase.Input(customerId, eventId);

    	// - when
    	/*
    	final var eventService = mock(EventService.class);
    	final var customerService = mock(CustomerService.class);
    	
    	when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
    	when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
    	when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.empty());
    	*/
    	
    	final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
    		useCase.Execute(subscribeInput);
    	});
	
    	// - then
    	Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
