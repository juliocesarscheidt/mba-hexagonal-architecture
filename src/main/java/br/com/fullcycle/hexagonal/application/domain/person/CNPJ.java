package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record CNPJ(String value) {

	public CNPJ {
		if (value == null || !value.matches("^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$")) {
			throw new ValidationException("Invalid value for CNPJ");
		}
	}
}
