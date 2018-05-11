package br.com.arvore.view;

import javax.swing.JPopupMenu;

import br.com.arvore.comp.MenuItem;
import br.com.arvore.util.Icones;

public class Popup extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	final MenuItem itemAtualizar = new MenuItem("label.atualizar", Icones.ATUALIZAR);
	final MenuItem itemRegistros = new MenuItem("label.registros", Icones.TABELA);
	final MenuItem itemConsulta = new MenuItem("label.filtro", Icones.FILTRO);

	public Popup() {
		add(itemRegistros);
		addSeparator();
		add(itemAtualizar);
		addSeparator();
		add(itemConsulta);
	}

	public void setHabilitarRegistros(boolean b) {
		itemRegistros.setEnabled(b);
	}

	public void setHabilitarConsulta(boolean b) {
		itemConsulta.setEnabled(b);
	}
}