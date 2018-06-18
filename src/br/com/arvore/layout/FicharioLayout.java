package br.com.arvore.layout;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.comp.TabbedPane;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.formulario.Formulario;
import br.com.arvore.util.Util;

public class FicharioLayout extends TabbedPane {
	private static final long serialVersionUID = 1L;
	private final List<FicharioLayoutListener> ouvintes;
	private final Formulario formulario;

	public FicharioLayout(Formulario formulario) {
		addChangeListener(e -> abaSelecionada());
		this.formulario = formulario;
		ouvintes = new ArrayList<>();
	}

	public void adicionarOuvinte(FicharioLayoutListener listener) {
		ouvintes.add(listener);
	}

	public void limpar() {
		while (getTabCount() > 0) {
			remove(0);
		}
	}

	private void abaSelecionada() {
		int indice = getSelectedIndex();

		if (indice != -1) {
			ContainerLayout container = (ContainerLayout) getComponentAt(indice);
			ouvintes.forEach(o -> o.containerSelecionado(container));
		}
	}

	private void notificarContainerExcluido(ContainerLayout container) {
		ouvintes.forEach(o -> o.containerExcluido(container));
	}

	public void setDividerLocation(int indice) {
		ContainerLayout containerLayout = (ContainerLayout) getComponentAt(indice);
		containerLayout.setDividerLocation();
	}

	public void addAba(boolean clonar) throws Exception {
		ContainerLayout containerLayout = new ContainerLayout();
		addTab("label.layout", containerLayout);

		Fichario fichario = new Fichario(formulario.getFicharioListener(), formulario.getRaiz());
		fichario.setSize(new Dimension(getWidth(), 0));
		containerLayout.adicionar(fichario);
		fichario.addAba(true);

		TituloLayout tituloLayout = new TituloLayout(this, clonar);
		tituloLayout.adicionarOuvinte(tituloLayoutListener);
		setTabComponentAt(getTabCount() - 1, tituloLayout);
		setDividerLocation(getTabCount() - 1);
	}

	private TituloLayoutListener tituloLayoutListener = new TituloLayoutListener() {
		@Override
		public void excluirAba(int indice) {
			ContainerLayout containerLayout = (ContainerLayout) getComponentAt(indice);
			notificarContainerExcluido(containerLayout);
			remove(indice);
		}

		@Override
		public void clonarAba() {
			try {
				addAba(false);
			} catch (Exception ex) {
				Util.stackTraceAndMessage("CLONAR LAYOUT", ex, FicharioLayout.this);
			}
		}
	};
}