package br.com.arvore.container;

public interface ContainerListener {
	public void pedidoAtualizarObjeto(Container container);

	public void pedidoDestacarObjeto(Container container);

	public void pedidoExcluirObjeto(Container container);

	public void selecionadoObjeto(Container container);
}