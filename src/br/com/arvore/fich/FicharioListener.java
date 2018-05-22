package br.com.arvore.fich;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;

public interface FicharioListener {
	public void abaSelecionada(Arvore arvore, Objeto objeto);

	public void arvoreExcluida(Arvore arvore);
}