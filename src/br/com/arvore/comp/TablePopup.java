package br.com.arvore.comp;

import javax.swing.JPopupMenu;

import br.com.arvore.comp.MenuItem;

public class TablePopup extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	final MenuItem itemCopiarComAspas = new MenuItem("label.copiar_com_aspas");
	final MenuItem itemCopiar = new MenuItem("label.copiar");
	private int tag;

	public TablePopup() {
		add(itemCopiar);
		addSeparator();
		add(itemCopiarComAspas);
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
}