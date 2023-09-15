package br.com.fullcycle.hexagonal.application.usecases;

public abstract class UnitUseCase<INPUT> {
	// 1 Regra - Cada caso de uso tem um Input. Nao retorna entidade, agregado nem objeto de valor
	// 2 Regra - O caso de uso implementa o padrao Command - com metodo Execute
	
	public abstract void Execute(INPUT input);
}
