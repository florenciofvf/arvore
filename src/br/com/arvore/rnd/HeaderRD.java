package br.com.arvore.rnd;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import br.com.arvore.comp.Label;
import br.com.arvore.util.Icones;

public class HeaderRD extends Label implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private final boolean descendente;
	private final boolean numero;

	public HeaderRD(boolean descendente, boolean numero) {
		this.descendente = !descendente;
		this.numero = numero;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int rowIndex, int vColIndex) {
		setText(value.toString());
		setToolTipText(value.toString());

		if (numero) {
			setIcon(descendente ? Icones.DESC_NUMERO : Icones.ASC_NUMERO);
		} else {
			setIcon(descendente ? Icones.DESC_TEXTO : Icones.ASC_TEXTO);
		}

		return this;
	}
}