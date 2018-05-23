package br.com.arvore.rd;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import br.com.arvore.Objeto;

public class TreeRD extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	public TreeRD() {
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value instanceof Objeto) {
			Objeto obj = (Objeto) value;

			if (obj.estaVazio() && obj.isManterVazio()) {
				setIcon(obj.getIconeManterVazio());
			} else {
				setIcon(obj.getIcone());
			}
		}

		return this;
	}
}