package br.com.arvore.layout;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.comp.TabbedPane;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.formulario.Formulario;
import br.com.arvore.util.Util;

public class LayoutFichario extends TabbedPane {
	private static final long serialVersionUID = 1L;
	private final List<LayoutFicharioListener> ouvintes;
	private final Formulario formulario;

	public LayoutFichario(Formulario formulario) {
		addChangeListener(e -> abaSelecionada());
		this.formulario = formulario;
		ouvintes = new ArrayList<>();
	}

	public void adicionarOuvinte(LayoutFicharioListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.add(listener);
	}

	public void excluirOuvinte(LayoutFicharioListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.remove(listener);
	}

	public void limpar() {
		while (getTabCount() > 0) {
			remove(0);
		}
	}

	private void abaSelecionada() {
		int indice = getSelectedIndex();

		if (indice != -1) {
			LayoutContainer container = (LayoutContainer) getComponentAt(indice);
			ouvintes.forEach(o -> o.containerSelecionado(container));
		}
	}

	private void notificarContainerExcluido(LayoutContainer container) {
		ouvintes.forEach(o -> o.containerExcluido(container));
	}

	public void setDividerLocation(int indice) {
		LayoutContainer container = (LayoutContainer) getComponentAt(indice);
		container.setDividerLocation();
	}

	public void adicionarAba(boolean principal) throws Exception {
		LayoutContainer container = new LayoutContainer();
		addTab("label.layout", container);

		Fichario fichario = new Fichario(formulario.getRaiz());
		fichario.adicionarOuvinte(formulario.getFicharioListener());
		fichario.setSize(new Dimension(getWidth(), 0));
		fichario.adicionarAba(true);
		container.set(fichario);

		TituloLayout titulo = new TituloLayout(this, principal);
		setTabComponentAt(getTabCount() - 1, titulo);
		titulo.adicionarOuvinte(tituloListener);
		setDividerLocation(getTabCount() - 1);
	}

	private LayoutTituloListener tituloListener = new LayoutTituloListener() {
		@Override
		public void excluirAba(int indice) {
			LayoutContainer container = (LayoutContainer) getComponentAt(indice);
			notificarContainerExcluido(container);
			remove(indice);
		}

		@Override
		public void clonarAba() {
			try {
				adicionarAba(false);
			} catch (Exception ex) {
				Util.stackTraceAndMessage("CLONAR LAYOUT", ex, LayoutFichario.this);
			}
		}
	};
}