package br.com.fullcycle.domain.partner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.domain.exceptions.ValidationException;

public class PartnerTest {

	@Test
    @DisplayName("Deve instanciar um parceiro")
    public void testInstantiatePartner() {
        // given
        final var expectedCNPJ = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        // when
        final var output = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

        // then
        Assertions.assertNotNull(output.getPartnerId());
        Assertions.assertEquals(expectedCNPJ, output.getCnpj().value());
        Assertions.assertEquals(expectedEmail, output.getEmail().value());
        Assertions.assertEquals(expectedName, output.getName().value());
    }
	
	@Test
    @DisplayName("Nao deve instanciar um parceiro com cnpj invalido")
    public void testInstantiatePartnerWithInvalidCpf() {
        // given
        final var expectedError = "Invalid value for CNPJ";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
        	Partner.newPartner("John Doe", "41536538000100", "john.doe@gmail.com");
        });

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
	
	@Test
    @DisplayName("Nao deve instanciar um parceiro com nome invalido")
    public void testInstantiatePartnerWithInvalidName() {
        // given
        final var expectedError = "Invalid value for Name";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
        	Partner.newPartner(null, "41.536.538/0001-00", "john.doe@gmail.com");
        });

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
	
	@Test
    @DisplayName("Nao deve instanciar um parceiro com email invalido")
    public void testInstantiatePartnerWithInvalidEmail() {
        // given
        final var expectedError = "Invalid value for Email";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
        	Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe.gmail.com");
        });

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
