package br.com.fullcycle.hexagonal.application.usecases;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.infrastructure.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.repositories.PartnerRepository;

public class GetPartnerByIdUseCaseIT extends IntegrationTest {

	@Autowired
	private GetPartnerByIdUseCase useCase;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	@BeforeEach
	void tearDown() {
		partnerRepository.deleteAll();
	}
	
	@Test
    @DisplayName("Deve obter um partner por id")
    public void testGetPartnerById() throws Exception {
		// tests steps:
		// - given
		final var expectedCNPJ = "41536538000100";
		final var expectedEmail = "john.doe@gmail.com";
		final var expectedName = "John Doe";

		final var aPartner = createPartner(expectedCNPJ, expectedEmail, expectedName);
		final var expectedId = aPartner.getId();

		final var input = new GetPartnerByIdUseCase.Input(expectedId);

		// - when
		/*
		final var partnerService = mock(PartnerService.class);
		when(partnerService.findById(expectedId)).thenReturn(Optional.of(aPartner));
		*/

		final var output = useCase.Execute(input).get();
		
		// - then
		Assertions.assertEquals(expectedId, output.id());
		Assertions.assertEquals(expectedCNPJ, output.cnpj());
		Assertions.assertEquals(expectedEmail, output.email());
		Assertions.assertEquals(expectedName, output.name());
    }
	
	@Test
    @DisplayName("Deve obter vazio ao tentar recuperar um partner nao existente")
    public void testGetInexistingPartnerById() throws Exception {
		// tests steps:
			// - given
		final var expectedId = UUID.randomUUID().getMostSignificantBits();

		final var input = new GetPartnerByIdUseCase.Input(expectedId);

		// - when
		/*
		final var partnerService = mock(PartnerService.class);
		when(partnerService.findById(expectedId)).thenReturn(Optional.empty());
		*/

		final var output = useCase.Execute(input);
		
		// - then
		Assertions.assertTrue(output.isEmpty());
    }
	
	private Partner createPartner(String cnpj, String email, String name) {
		final var aPartner = new Partner();
		aPartner.setCnpj(cnpj);
		aPartner.setEmail(email);
		aPartner.setName(name);
		
		return partnerRepository.save(aPartner);
	}
}
