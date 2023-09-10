package br.com.fullcycle.hexagonal.application;

public abstract class NullaryUseCase<OUTPUT> {
	// 1 Regra - Cada caso de uso tem um Output. Nao retorna entidade, agregado nem objeto de valor
	// 2 Regra - O caso de uso implementa o padrao Command - com metodo Execute

	public abstract OUTPUT Execute();
}
