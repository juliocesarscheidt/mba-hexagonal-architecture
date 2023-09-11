package br.com.fullcycle.hexagonal.application.usecases;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.PartnerService;

public class CreatePartnerUseCaseTest {
	
    @Test
    @DisplayName("Deve criar um partner")
    public void testCreatePartner() throws ValidationException {
    	// tests steps:
    	// - given
    	final var expectedCNPJ = "41536538000100";
    	final var expectedEmail = "john.doe@gmail.com";
    	final var expectedName = "John Doe";

    	final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    	// - when
    	final var partnerService = mock(PartnerService.class);
    	when(partnerService.findByCnpj(expectedCNPJ)).thenReturn(Optional.empty());
    	when(partnerService.findByEmail(expectedEmail)).thenReturn(Optional.empty());
    	when(partnerService.save(any())).thenAnswer(a -> {
    		var partner = a.getArgument(0, Partner.class);
    		partner.setId(UUID.randomUUID().getMostSignificantBits());
    		return partner;
    	}); // returns the first argument passed

    	final var useCase = new CreatePartnerUseCase(partnerService);
    	final var output = useCase.Execute(createInput);
    	// System.out.println(output);

    	// - then
    	Assertions.assertNotNull(output.id());
    	Assertions.assertEquals(expectedCNPJ, output.cnpj());
    	Assertions.assertEquals(expectedEmail, output.email());
    	Assertions.assertEquals(expectedName, output.name());
    }
    
    @Test
    @DisplayName("Não deve cadastrar um partner com CNPJ duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws ValidationException {
    	// tests steps:
    	// - given
    	final var expectedCNPJ = "41536538000100";
    	final var expectedEmail = "john.doe@gmail.com";
    	final var expectedName = "John Doe";
    	final var expectedError = "Partner already exists";

    	final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    	final var aPartner = new Partner();
    	aPartner.setId(UUID.randomUUID().getMostSignificantBits());
    	aPartner.setCnpj(expectedCNPJ);
    	aPartner.setEmail(expectedEmail);
    	aPartner.setName(expectedName);

    	// - when
    	final var partnerService = mock(PartnerService.class);
    	when(partnerService.findByCnpj(expectedCNPJ)).thenReturn(Optional.of(aPartner));

    	final var useCase = new CreatePartnerUseCase(partnerService);
    	final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.Execute(createInput));
    	// System.out.println(actualException.getMessage());
    	// Partner already exists
	
    	// - then
    	Assertions.assertEquals(expectedError, actualException.getMessage());
    }
    
    @Test
    @DisplayName("Não deve cadastrar um partner com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws ValidationException {
    	// tests steps:
    	// - given
    	final var expectedCNPJ = "41536538000100";
    	final var expectedEmail = "john.doe@gmail.com";
    	final var expectedName = "John Doe";
    	final var expectedError = "Partner already exists";

    	final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    	final var aPartner = new Partner();
    	aPartner.setId(UUID.randomUUID().getMostSignificantBits());
    	aPartner.setCnpj(expectedCNPJ);
    	aPartner.setEmail(expectedEmail);
    	aPartner.setName(expectedName);

    	// - when
    	final var partnerService = mock(PartnerService.class);
    	when(partnerService.findByEmail(expectedEmail)).thenReturn(Optional.of(aPartner));

    	final var useCase = new CreatePartnerUseCase(partnerService);
    	final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.Execute(createInput));
    	// System.out.println(actualException.getMessage());
    	// Partner already exists
	
    	// - then
    	Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
