package br.com.arvore.view;

import javax.swing.JPopupMenu;

import br.com.arvore.comp.MenuItem;
import br.com.arvore.util.Icones;

public class Popup extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	final MenuItem itemAtualizar = new MenuItem("label.atualizar", Icones.ATUALIZAR);
	final MenuItem itemRegistros = new MenuItem("label.registros", Icones.TABELA);
	final MenuItem itemExcluir = new MenuItem("label.excluir", Icones.LIXEIRA);
	final MenuItem itemFiltro = new MenuItem("label.filtro", Icones.FILTRO);

	public Popup() {
		add(itemRegistros);
		addSeparator();
		add(itemAtualizar);
		addSeparator();
		add(itemExcluir);
		addSeparator();
		add(itemFiltro);
	}

	public void setHabilitarRegistros(boolean b) {
		itemRegistros.setEnabled(b);
	}

	public void setHabilitarExcluir(boolean b) {
		itemExcluir.setEnabled(b);
	}

	public void setHabilitarFiltro(boolean b) {
		itemFiltro.setEnabled(b);
	}
}