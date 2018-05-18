package br.com.arvore.comp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import br.com.arvore.Objeto;
import br.com.arvore.util.TreeRD;

public class Arvore extends JTree {
	private static final long serialVersionUID = 1L;
	private final List<ArvoreListener> ouvintes;

	public Arvore(TreeModel newModel) {
		super(newModel);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		putClientProperty("JTree.lineStyle", "Horizontal");
		addMouseListener(new Listener());
		setCellRenderer(new TreeRD());
		ouvintes = new ArrayList<>();
		setShowsRootHandles(true);
		setRootVisible(true);
	}

	public void adicionarOuvinte(ArvoreListener arvoreListener) {
		int indice = ouvintes.indexOf(arvoreListener);

		if (indice == -1) {
			ouvintes.add(arvoreListener);
		}
	}

	public void excluirOuvinte(ArvoreListener arvoreListener) {
		ouvintes.remove(arvoreListener);
	}

	private void notificarClicado(Objeto objeto) {
		for (ArvoreListener ouvinte : ouvintes) {
			ouvinte.clicado(this, objeto);
		}
	}

	private void notificarExibirPopup(Objeto objeto, MouseEvent e) {
		for (ArvoreListener ouvinte : ouvintes) {
			ouvinte.exibirPopup(this, objeto, e);
		}
	}

	private class Listener extends MouseAdapter {
		Objeto ultimoSelecionado;

		@Override
		public void mouseClicked(MouseEvent e) {
			TreePath path = getSelectionPath();

			if (path == null) {
				return;
			}

			if (path.getLastPathComponent() instanceof Objeto) {
				Objeto selecionado = (Objeto) path.getLastPathComponent();

				if (ultimoSelecionado != selecionado) {
					ultimoSelecionado = selecionado;
					notificarClicado(selecionado);
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

			if (path.getLastPathComponent() instanceof Objeto) {
				Objeto selecionado = (Objeto) path.getLastPathComponent();
				notificarExibirPopup(selecionado, e);
			}
		}
	}
}