package br.com.arvore.util;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import br.com.arvore.Objeto;

public class TreeCellRD extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	public TreeCellRD() {
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value instanceof Objeto) {
			Objeto obj = (Objeto) value;

			setIcon(obj.getIcone());
		}

		return this;
	}
}