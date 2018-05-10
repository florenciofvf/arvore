package br.com.arvore.comp;

import javax.swing.table.TableColumn;

public interface TableListener {
	public void ordenarColuna(TableColumn tableColumn, boolean descendente, int modelColuna);
}