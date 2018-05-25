package br.com.arvore.arvore;

public interface ArvoreListener {
	public void pedidoAtualizarObjeto(Arvore arvore);

	public void pedidoDestacarObjeto(Arvore arvore);

	public void pedidoExcluirObjeto(Arvore arvore);

	public void selecionadoObjeto(Arvore arvore);
}