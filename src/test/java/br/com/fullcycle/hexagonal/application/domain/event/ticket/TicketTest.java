package br.com.fullcycle.hexagonal.application.domain.event.ticket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;

public class TicketTest {

	@Test
    @DisplayName("Deve criar um ticket")
    public void testReserveTicket() throws Exception {
        // given
		final var partner =
			Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var customer =
    		Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");
        final var event =
    		Event.newEvent("Disney on Ice", "2021-01-01", 10, partner);

        final var expectedTicketStatus = TicketStatus.PENDING;
        final var expectedEventId = event.getEventId();
        final var expectedCustomerId = customer.getCustomerId();

        // when
        final var actualTicket = Ticket.newTicket(customer.getCustomerId(), event.getEventId());

        // then
        Assertions.assertNotNull(actualTicket.getTicketId());
        Assertions.assertNotNull(actualTicket.getReservedAt());
        Assertions.assertNull(actualTicket.getPaidAt());
        Assertions.assertEquals(expectedEventId, actualTicket.getEventId());
        Assertions.assertEquals(expectedCustomerId, actualTicket.getCustomerId());
        Assertions.assertEquals(expectedTicketStatus, actualTicket.getStatus());
    }
}
