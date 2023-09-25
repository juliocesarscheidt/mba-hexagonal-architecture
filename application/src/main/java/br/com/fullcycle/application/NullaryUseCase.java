package br.com.fullcycle.application;

public abstract class NullaryUseCase<OUTPUT> {
	// 1 Regra - Cada caso de uso tem um Output. Nao retorna entidade, agregado nem objeto de valor
	// 2 Regra - O caso de uso implementa o padrao Command - com metodo Execute

	public abstract OUTPUT Execute();
	
	public <T> T Execute(Presenter<OUTPUT, T> presenter) throws Exception {
		try {
			return presenter.present(Execute());
		} catch (Throwable t) {
			return presenter.present(t);
		}
	}
}
