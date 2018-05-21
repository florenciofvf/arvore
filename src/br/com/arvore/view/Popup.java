package br.com.arvore.view;

import javax.swing.JPopupMenu;

import br.com.arvore.comp.MenuItem;
import br.com.arvore.util.Icones;

public class Popup extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	final MenuItem itemAtualizar = new MenuItem("label.atualizar", Icones.ATUALIZAR);
	final MenuItem itemDestacar = new MenuItem("label.destacar", Icones.DESCONECTA);

	public Popup() {
		add(itemAtualizar);
		addSeparator();
		add(itemDestacar);
	}
}