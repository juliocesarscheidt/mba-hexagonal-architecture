package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Event;
import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.EventService;
import br.com.fullcycle.hexagonal.services.PartnerService;
import io.hypersistence.tsid.TSID;

public class CreateEventUseCaseTest {

	@Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() throws Exception {
		// tests steps:
    	// - given
		final var expectedDate = "2021-01-01";
		final var expectedName = "Disney on Ice";
		final var expectedTotalSpots = 100;
		final var expectedPartnerId = TSID.fast().toLong();
		
        final var createInput =
    		new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots);
        
        // when
        final var eventService = Mockito.mock(EventService.class);
        final var partnerService = Mockito.mock(PartnerService.class);

        Mockito.when(eventService.save(any())).thenAnswer(a -> {
        	final var event = a.getArgument(0, Event.class);
        	event.setId(TSID.fast().toLong());
        	return event;
        });
        
        Mockito.when(partnerService.findById(eq(expectedPartnerId))).thenReturn(Optional.of(new Partner()));
        
        final var useCase = new CreateEventUseCase(eventService, partnerService);
        final var output = useCase.Execute(createInput);
        
        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDate, output.date());
        Assertions.assertEquals(expectedTotalSpots, output.totalSpots());
        Assertions.assertEquals(expectedPartnerId, output.partnerId());
    }

	@Test
    @DisplayName("Nao deve criar um evento quando o partner nao existir")
    public void testCreateEventWhenPartnerDoesntExist() throws Exception {
		// tests steps:
    	// - given
		final var expectedDate = "2021-01-01";
		final var expectedName = "Disney on Ice";
		final var expectedTotalSpots = 100;
		final var expectedPartnerId = TSID.fast().toLong();
		final var expectedError = "Partner not found";
		
        final var createInput =
    		new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots);

        // when
        final var eventService = Mockito.mock(EventService.class);
        final var partnerService = Mockito.mock(PartnerService.class);

        Mockito.when(partnerService.findById(eq(expectedPartnerId))).thenReturn(Optional.empty());
    
        final var useCase = new CreateEventUseCase(eventService, partnerService);
        final var actualException = assertThrows(ValidationException.class, () -> {
        	useCase.Execute(createInput);
        });
        
        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
