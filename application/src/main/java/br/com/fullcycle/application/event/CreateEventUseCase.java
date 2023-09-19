package br.com.fullcycle.application.event;

import java.util.Objects;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.partner.PartnerRepository;

public class CreateEventUseCase
	extends UseCase<CreateEventUseCase.Input, CreateEventUseCase.Output> {

	public record Input(String date, String name, String partnerId, Integer totalSpots) {}
	
	public record Output(String id, String date, String name, Integer totalSpots, String partnerId) {}
	
	private final EventRepository eventRepository;
	private final PartnerRepository partnerRepository;

	public CreateEventUseCase(final EventRepository eventRepository, final PartnerRepository partnerRepository) {
		this.eventRepository = Objects.requireNonNull(eventRepository);
		this.partnerRepository = Objects.requireNonNull(partnerRepository);
	}
	
	@Override
	public Output Execute(final Input input) throws ValidationException {
        final var partner = partnerRepository.partnerOfId(PartnerId.with(input.partnerId))
        	.orElseThrow(() -> new ValidationException("Partner not found"));
        
        final var event = eventRepository.create(Event.newEvent(input.name, input.date, input.totalSpots, partner));

        return new Output(
        	event.getEventId().value(),
        	input.date,
        	event.getName().value(),
        	event.getTotalSpots(),
        	event.getPartnerId().value()
    	);
	}
}
