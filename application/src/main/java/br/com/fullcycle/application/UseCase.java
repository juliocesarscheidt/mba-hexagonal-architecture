package br.com.fullcycle.application;

public abstract class UseCase<INPUT, OUTPUT> {
	// 1 Regra - Cada caso de uso tem um Input e Output. Nao retorna entidade, agregado nem objeto de valor
	// 2 Regra - O caso de uso implementa o padrao Command - com metodo Execute

	public abstract OUTPUT Execute(INPUT input) throws Exception;

	public <T> T Execute(INPUT input, Presenter<OUTPUT, T> presenter) throws Exception {
		try {
			return presenter.present(Execute(input));
		} catch (Throwable t) {
			return presenter.present(t);
		}
	}
}
