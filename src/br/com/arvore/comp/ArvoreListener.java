package br.com.arvore.comp;

import java.awt.event.MouseEvent;

import br.com.arvore.Objeto;

public interface ArvoreListener {
	public void exibirPopup(Arvore arvore, Objeto selecionado, MouseEvent e);
	public void clicado(Objeto objeto);
}