package br.com.arvore.ficha;

import br.com.arvore.Objeto;
import br.com.arvore.arvore.Arvore;

public interface FicharioListener {
	public void abaSelecionada(Arvore arvore, Objeto objeto);

	public void arvoreExcluida(Arvore arvore);
}