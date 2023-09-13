package br.com.fullcycle.hexagonal.application.usecases.event;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infrastructure.repositories.PartnerRepository;
import io.hypersistence.tsid.TSID;

public class CreateEventUseCaseIT extends IntegrationTest {

	@Autowired
	private CreateEventUseCase useCase;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	@BeforeEach
	void tearDown() {
		eventRepository.deleteAll();
		partnerRepository.deleteAll();
	}
	
	@Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() throws Exception {
		// tests steps:
    	// - given
		final var aPartner = createPartner("41536538000100", "john.doe@gmail.com", "John Doe");

		final var expectedDate = "2021-01-01";
		final var expectedName = "Disney on Ice";
		final var expectedTotalSpots = 100;
		final var expectedPartnerId = aPartner.getId();

        final var createInput =
    		new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots);
        
        // when
        /*
        final var eventService = Mockito.mock(EventService.class);
        final var partnerService = Mockito.mock(PartnerService.class);
        */

        /*
        Mockito.when(eventService.save(any())).thenAnswer(a -> {
        	final var event = a.getArgument(0, Event.class);
        	event.setId(TSID.fast().toLong());
        	return event;
        });
        
        Mockito.when(partnerService.findById(eq(expectedPartnerId))).thenReturn(Optional.of(new Partner()));
        */
        
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
        /*
        final var eventService = Mockito.mock(EventService.class);
        final var partnerService = Mockito.mock(PartnerService.class);

        Mockito.when(partnerService.findById(eq(expectedPartnerId))).thenReturn(Optional.empty());
        */
    
        final var actualException = assertThrows(ValidationException.class, () -> {
        	useCase.Execute(createInput);
        });
        
        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
	
	private Partner createPartner(String cnpj, String email, String name) {
		final var aPartner = new Partner();
		aPartner.setCnpj(cnpj);
		aPartner.setEmail(email);
		aPartner.setName(name);
		
		return partnerRepository.save(aPartner);
	}
}
