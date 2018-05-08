package br.com.arvore.comp;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class Table extends JTable {
	private static final long serialVersionUID = 1L;

	public Table(TableModel dm) {
		super(dm);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
}