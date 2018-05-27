package br.com.arvore.arvore;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import br.com.arvore.Objeto;
import br.com.arvore.ObjetoUtil;
import br.com.arvore.componente.MenuItem;
import br.com.arvore.componente.Popup;
import br.com.arvore.renderer.TreeRD;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Util;

public class Arvore extends JTree {
	private static final long serialVersionUID = 1L;
	private ArvorePopup arvorePopup = new ArvorePopup();
	private final List<ArvoreListener> ouvintes;

	public Arvore(TreeModel newModel) {
		super(newModel);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		putClientProperty("JTree.lineStyle", "Horizontal");
		addMouseListener(mouseListener);
		setCellRenderer(new TreeRD());
		ouvintes = new ArrayList<>();
		setShowsRootHandles(true);
		setRootVisible(true);

		arvorePopup.itemAtualizar.addActionListener(e -> ouvintes.forEach(o -> o.pedidoAtualizarObjeto(this)));
		arvorePopup.itemDestacar.addActionListener(e -> ouvintes.forEach(o -> o.pedidoDestacarObjeto(this)));
		arvorePopup.itemDelete.addActionListener(e -> ouvintes.forEach(o -> o.pedidoExcluirObjeto(this)));
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
		Objeto ultimoSelecionado;

		@Override
		public void mouseClicked(MouseEvent e) {
			Objeto selecionado = getObjetoSelecionado();

			if (selecionado != null && ultimoSelecionado != selecionado) {
				ultimoSelecionado = selecionado;
				ouvintes.forEach(o -> o.selecionadoObjeto(Arvore.this));
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

			Objeto selecionado = getObjetoSelecionado();

			if (selecionado != null) {
				arvorePopup.itemDelete.setEnabled(!Util.estaVazio(selecionado.getInstrucaoDelete()));
				arvorePopup.show(Arvore.this, e.getX(), e.getY());
			}
		}
	};

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
		}
	}
}