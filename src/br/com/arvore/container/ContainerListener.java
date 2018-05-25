package br.com.arvore.container;

public interface ContainerListener {
	public void pedidoAtualizar(Container container);

	public void pedidoDestacar(Container container);

	public void pedidoExcluir(Container container);

	public void selecionado(Container container);
}