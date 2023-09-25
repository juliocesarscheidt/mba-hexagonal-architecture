package br.com.fullcycle.domain.event;

import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;

public class EventTest {

	@Test
    @DisplayName("Deve criar um evento sem nenhm ticket")
    public void testCreate() throws Exception {
        // given
        final var partner =
            Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var expectedDate = "2021-01-01T00:00:00";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = partner.getPartnerId().value();
        final var expectedTicketsSize = 0;

        // when
        final var event = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);

        // then
        Assertions.assertNotNull(event.getEventId());
        Assertions.assertEquals(expectedDate, event.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        Assertions.assertEquals(expectedName, event.getName().value());
        Assertions.assertEquals(expectedTotalSpots, event.getTotalSpots());
        Assertions.assertEquals(expectedPartnerId, event.getPartnerId().value());
        Assertions.assertEquals(expectedTicketsSize, event.getTickets().size());
    }
	
	@Test
    @DisplayName("Nao deve criar um evento com nome invalido")
    public void testCreateWithInvalidName() throws Exception {
        // given
        final var aPartner =
                Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var expectedError = "Invalid value for Name";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
        	Event.newEvent(null, "2021-01-01T00:00:00", 10, aPartner);
        });

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
	
	@Test
    @DisplayName("Nao deve criar um evento com data invalida")
    public void testCreateWithInvalidDate() throws Exception {
        // given
        final var aPartner =
                Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var expectedError = "Invalid date for Event";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
        	Event.newEvent("Disney on Ice", "20210101", 10, aPartner);
        });

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
	
	@Test
    @DisplayName("Nao reservar um ticket quando possivel")
    public void testReserveTicket() throws Exception {
		// given
        final var expectedDate = "2021-01-01T00:00:00";;
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedTicketsSize = 1;
        final var expectedTicketOrdering = 1;

		final var partner =
			Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var customer =
    		Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");
        final var event =
    		Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);

        final var expectedPartnerId = partner.getPartnerId().value();
        final var expectedCustomerId = customer.getCustomerId();
        final var expectedEventId = event.getEventId();
        // final var expectedTicketStatus = TicketStatus.PENDING;

        // when
        final var ticket = event.reserveTicket(expectedCustomerId);

        // then
        // ticket checks
        Assertions.assertNotNull(ticket.getEventTicketId());
        // Assertions.assertNotNull(ticket.getReservedAt());
        // Assertions.assertNull(ticket.getPaidAt());
        Assertions.assertEquals(expectedEventId, ticket.getEventId());
        Assertions.assertEquals(expectedCustomerId, ticket.getCustomerId());
        // Assertions.assertEquals(expectedTicketStatus, ticket.getStatus());
        
        Assertions.assertEquals(expectedDate, event.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        Assertions.assertEquals(expectedName, event.getName().value());
        Assertions.assertEquals(expectedTotalSpots, event.getTotalSpots());
        Assertions.assertEquals(expectedPartnerId, event.getPartnerId().value());
        Assertions.assertEquals(expectedTicketsSize, event.getTickets().size());
        
        final var eventTicket = event.getTickets().iterator().next();
        Assertions.assertEquals(expectedTicketOrdering, eventTicket.getOrdering());
        Assertions.assertEquals(expectedEventId, eventTicket.getEventId());
        Assertions.assertEquals(expectedCustomerId, eventTicket.getCustomerId());
        Assertions.assertEquals(ticket.getTicketId(), eventTicket.getTicketId());
    }
	
	@Test
    @DisplayName("Não deve reservar um ticket quando o evento está esgotado")
    public void testReserveTicketWhenEventIsSoldOut() throws Exception {
        // given
		final var partner =
			Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var customer1 =
    		Customer.newCustomer("John1 Doe", "123.456.789-01", "john1.doe@gmail.com");
        final var customer2 =
    		Customer.newCustomer("John2 Doe", "123.456.789-01", "john2.doe@gmail.com");

        final var expectedTotalSpots = 1;
        final var expectedError = "Event sold out";

        final var event = Event.newEvent("Disney on Ice", "2021-01-01T00:00:00", expectedTotalSpots, partner);

        event.reserveTicket(customer1.getCustomerId());

        // when
        final var actualError = Assertions.assertThrows(
            ValidationException.class,
            () -> event.reserveTicket(customer2.getCustomerId())
        );

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
	
	@Test
    @DisplayName("Não deve reservar dois tickets para um mesmo cliente")
    public void testReserveTwoTicketsForTheSameClient() throws Exception {
        // given
		final var partner =
			Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var customer =
    		Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");

        final var expectedTotalSpots = 1;
        final var expectedError = "Email already registered";

        final var event = Event.newEvent("Disney on Ice", "2021-01-01T00:00:00", expectedTotalSpots, partner);

        event.reserveTicket(customer.getCustomerId());

        // when
        final var actualError = Assertions.assertThrows(
            ValidationException.class,
            () -> event.reserveTicket(customer.getCustomerId())
        );

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
}
