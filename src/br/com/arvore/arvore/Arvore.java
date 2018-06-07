package br.com.arvore.arvore;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import br.com.arvore.Objeto;
import br.com.arvore.ObjetoUtil;
import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.Popup;
import br.com.arvore.renderer.TreeRD;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Util;

public class Arvore extends JTree {
	private static final long serialVersionUID = 1L;
	private ArvorePopup arvorePopup = new ArvorePopup();
	private final List<ArvoreListener> ouvintes;
	private boolean popupDesabilitado;

	public Arvore(TreeModel newModel) {
		super(newModel);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		putClientProperty("JTree.lineStyle", "Horizontal");
		setBorder(BorderFactory.createEmptyBorder());
		addMouseListener(mouseListener);
		setCellRenderer(new TreeRD());
		ouvintes = new ArrayList<>();
		setShowsRootHandles(true);
		setRootVisible(true);
	}

	public void adicionarOuvinte(ArvoreListener listener) {
		ouvintes.add(listener);
	}

	public Objeto getObjetoSelecionado() {
		TreePath path = getSelectionPath();

		if (path == null) {
			return null;
		}

		if (path.getLastPathComponent() instanceof Objeto) {
			return (Objeto) path.getLastPathComponent();
		}

		return null;
	}

	public void excluirSelecionado() {
		Objeto selecionado = getObjetoSelecionado();

		if (selecionado == null) {
			return;
		}

		ArvoreUtil.excluirEstrutura(this, selecionado);
	}

	public void inflarSelecionado() throws Exception {
		Objeto selecionado = getObjetoSelecionado();

		if (selecionado == null) {
			return;
		}

		ObjetoUtil.inflar(selecionado);
		ArvoreUtil.atualizarEstrutura(this, selecionado);
	}

	private MouseListener mouseListener = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			ouvintes.forEach(o -> o.selecionadoObjeto(Arvore.this));
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

			Objeto selecionado = getObjetoSelecionado();

			if (selecionado != null && !popupDesabilitado) {
				arvorePopup.itemDelete.setEnabled(!Util.estaVazio(selecionado.getGrupoDelete()));
				arvorePopup.show(Arvore.this, e.getX(), e.getY());
			}
		}
	};

	public void desabilitarPopup() {
		popupDesabilitado = true;
	}

	private class ArvorePopup extends Popup {
		private static final long serialVersionUID = 1L;
		final MenuItem itemAtualizar = new MenuItem("label.atualizar", Icones.ATUALIZAR);
		final MenuItem itemDestacar = new MenuItem("label.destacar", Icones.DESCONECTAR);
		final MenuItem itemDelete = new MenuItem("label.delete", Icones.EXCLUIR);

		public ArvorePopup() {
			add(itemAtualizar);
			addSeparator();
			add(itemDestacar);
			addSeparator();
			add(itemDelete);

			itemAtualizar.addActionListener(e -> ouvintes.forEach(o -> o.pedidoAtualizarObjeto(Arvore.this)));
			itemDestacar.addActionListener(e -> ouvintes.forEach(o -> o.pedidoDestacarObjeto(Arvore.this)));
			itemDelete.addActionListener(e -> ouvintes.forEach(o -> o.pedidoExcluirObjeto(Arvore.this)));
		}
	}
}