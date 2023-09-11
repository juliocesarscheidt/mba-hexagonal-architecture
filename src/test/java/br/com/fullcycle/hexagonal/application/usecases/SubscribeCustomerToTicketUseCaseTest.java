package br.com.fullcycle.hexagonal.application.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.models.Event;
import br.com.fullcycle.hexagonal.models.Ticket;
import br.com.fullcycle.hexagonal.models.TicketStatus;
import br.com.fullcycle.hexagonal.services.CustomerService;
import br.com.fullcycle.hexagonal.services.EventService;
import io.hypersistence.tsid.TSID;

public class SubscribeCustomerToTicketUseCaseTest {

	@Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() throws Exception {
		// tests steps:
    	// - given
		final var expectedTicketSize = 1;
		final var customerId = TSID.fast().toLong();
    	
    	final var eventId = TSID.fast().toLong();
    	final var aEvent = new Event();
    	aEvent.setId(eventId);
    	aEvent.setName("Disney");
    	aEvent.setTotalSpots(10);

    	final var subscribeInput =
			new SubscribeCustomerToTicketUseCase.Input(customerId, eventId);

    	// - when
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

    	final var useCase = new SubscribeCustomerToTicketUseCase(eventService, customerService);
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
		final var customerId = TSID.fast().toLong();
    	
    	final var eventId = TSID.fast().toLong();
    
    	final var subscribeInput =
			new SubscribeCustomerToTicketUseCase.Input(customerId, eventId);

    	// - when
    	final var eventService = mock(EventService.class);
    	final var customerService = mock(CustomerService.class);
    	
    	when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
    	when(eventService.findById(eventId)).thenReturn(Optional.empty());

    	final var useCase = new SubscribeCustomerToTicketUseCase(eventService, customerService);
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
    	final var eventService = mock(EventService.class);
    	final var customerService = mock(CustomerService.class);
    	
    	when(customerService.findById(customerId)).thenReturn(Optional.empty());

    	final var useCase = new SubscribeCustomerToTicketUseCase(eventService, customerService);
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
		final var customerId = TSID.fast().toLong();
    	
    	final var eventId = TSID.fast().toLong();
    	final var aEvent = new Event();
    	aEvent.setId(eventId);
    	aEvent.setName("Disney");
    	aEvent.setTotalSpots(10);

    	final var subscribeInput =
			new SubscribeCustomerToTicketUseCase.Input(customerId, eventId);

    	// - when
    	final var eventService = mock(EventService.class);
    	final var customerService = mock(CustomerService.class);
    	
    	when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
    	when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
    	when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.of(new Ticket()));
    	
    	final var useCase = new SubscribeCustomerToTicketUseCase(eventService, customerService);
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
		final var customerId = TSID.fast().toLong();
    	
    	final var eventId = TSID.fast().toLong();
    	final var aEvent = new Event();
    	aEvent.setId(eventId);
    	aEvent.setName("Disney");
    	aEvent.setTotalSpots(0);

    	final var subscribeInput =
			new SubscribeCustomerToTicketUseCase.Input(customerId, eventId);

    	// - when
    	final var eventService = mock(EventService.class);
    	final var customerService = mock(CustomerService.class);
    	
    	when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
    	when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
    	when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.empty());
    	
    	final var useCase = new SubscribeCustomerToTicketUseCase(eventService, customerService);
    	final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
    		useCase.Execute(subscribeInput);
    	});
	
    	// - then
    	Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
