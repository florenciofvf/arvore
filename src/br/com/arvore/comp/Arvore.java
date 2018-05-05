package br.com.arvore.comp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import br.com.arvore.Objeto;

public class Arvore extends JTree {
	private static final long serialVersionUID = 1L;
	private final ArvoreListener arvoreListener;

	public Arvore(TreeModel newModel, ArvoreListener arvoreListener) {
		super(newModel);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		putClientProperty("JTree.lineStyle", "Horizontal");
		this.arvoreListener = arvoreListener;
		addMouseListener(new OuvinteArvore());
	}

	private class OuvinteArvore extends MouseAdapter {
		Objeto ultimoSelecionado;

		@Override
		public void mouseClicked(MouseEvent e) {
			TreePath path = getSelectionPath();

			if (path == null) {
				return;
			}

			if (path.getLastPathComponent() instanceof Objeto) {
				Objeto selecionado = (Objeto) path.getLastPathComponent();

				if (ultimoSelecionado != selecionado && arvoreListener != null) {
					ultimoSelecionado = selecionado;
					arvoreListener.selecionado(selecionado);
				}
			}
		}
	}
}