package br.com.arvore.view;

import javax.swing.table.TableColumn;

public interface TabelaListener {
	public void ordenarColuna(TableColumn tableColumn, boolean descendente, int modelColuna);
}