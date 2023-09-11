package br.com.fullcycle.hexagonal.application.usecases;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.PartnerService;

public class GetPartnerByIdUseCaseTest {

	@Test
    @DisplayName("Deve obter um partner por id")
    public void testGetPartnerById() throws Exception {
		// tests steps:
		// - given
		final var expectedId = UUID.randomUUID().getMostSignificantBits();
		final var expectedCNPJ = "41536538000100";
		final var expectedEmail = "john.doe@gmail.com";
		final var expectedName = "John Doe";

		final var aPartner = new Partner();
		aPartner.setId(expectedId);
		aPartner.setCnpj(expectedCNPJ);
		aPartner.setEmail(expectedEmail);
		aPartner.setName(expectedName);

		final var input = new GetPartnerByIdUseCase.Input(expectedId);

		// - when
		final var partnerService = mock(PartnerService.class);
		when(partnerService.findById(expectedId)).thenReturn(Optional.of(aPartner));

		final var useCase = new GetPartnerByIdUseCase(partnerService);
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
		final var partnerService = mock(PartnerService.class);
		when(partnerService.findById(expectedId)).thenReturn(Optional.empty());

		final var useCase = new GetPartnerByIdUseCase(partnerService);
		final var output = useCase.Execute(input);
		
		// - then
		Assertions.assertTrue(output.isEmpty());
    }
}
