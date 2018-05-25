package br.com.arvore.fichario;

import br.com.arvore.container.Container;

public interface FicharioListener {
	public void pedidoAtualizarObjeto(Container container);

	public void pedidoDestacarObjeto(Container container);

	public void containerSelecionado(Container container);

	public void pedidoExcluirObjeto(Container container);

	public void selecionadoObjeto(Container container);

	public void containerExcluido(Container container);
}