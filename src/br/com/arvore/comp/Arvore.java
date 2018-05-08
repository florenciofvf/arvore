package br.com.arvore.comp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import br.com.arvore.Objeto;
import br.com.arvore.util.TreeCellRD;

public class Arvore extends JTree {
	private static final long serialVersionUID = 1L;
	private final ArvoreListener arvoreListener;

	public Arvore(TreeModel newModel, ArvoreListener arvoreListener) {
		super(newModel);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		putClientProperty("JTree.lineStyle", "Horizontal");
		addMouseListener(new OuvinteArvore());
		this.arvoreListener = arvoreListener;
		setCellRenderer(new TreeCellRD());
		setShowsRootHandles(true);
		setRootVisible(true);
	}

	private class OuvinteArvore extends MouseAdapter {
		Objeto ultimoSelecionado;

		@Override
		public void mouseClicked(MouseEvent e) {
			TreePath path = getSelectionPath();

			if (path == null) {
				return;
			}

			if (path.getLastPathComponent() instanceof Objeto && arvoreListener != null) {
				Objeto selecionado = (Objeto) path.getLastPathComponent();

				if (ultimoSelecionado != selecionado) {
					ultimoSelecionado = selecionado;
					arvoreListener.clicado(selecionado);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			processar(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			processar(e);
		}

		private void processar(MouseEvent e) {
			if (!e.isPopupTrigger()) {
				return;
			}

			TreePath path = getSelectionPath();

			if (path == null) {
				return;
			}

			if (path.getLastPathComponent() instanceof Objeto && arvoreListener != null) {
				Objeto selecionado = (Objeto) path.getLastPathComponent();
				arvoreListener.exibirPopup(Arvore.this, selecionado, e);
			}
		}
	}
}