package br.com.arvore.tabela;

import javax.swing.table.TableColumn;

public interface TabelaListener {
	public void ordenarColuna(TableColumn tableColumn, boolean descendente, int modelColuna);
}