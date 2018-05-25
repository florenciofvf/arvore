package br.com.arvore.arvore;

public interface ArvoreListener {
	public void pedidoAtualizar(Arvore arvore);

	public void pedidoDestacar(Arvore arvore);

	public void pedidoExcluir(Arvore arvore);

	public void selecionado(Arvore arvore);
}