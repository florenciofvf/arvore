package br.com.arvore.fichariol;

import br.com.arvore.containerl.ContainerL;

public interface FicharioLListener {
	public void containerSelecionado(ContainerL container);

	public void containerExcluido(ContainerL container);
}