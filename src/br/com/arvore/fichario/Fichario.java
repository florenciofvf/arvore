package br.com.arvore.fichario;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.ObjetoUtil;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.Objeto;
import br.com.arvore.container.Container;
import br.com.arvore.container.ContainerListener;
import br.com.arvore.divisor.DivisorClone;
import br.com.arvore.formulario.Formulario;
import br.com.arvore.titulo.Titulo;
import br.com.arvore.titulo.TituloListener;
import br.com.arvore.util.Util;

public class Fichario extends TabbedPane implements DivisorClone {
	private static final long serialVersionUID = 1L;
	private final List<FicharioListener> ouvintes;
	private final Formulario formulario;
	private Objeto raiz;

	public Fichario(Formulario formulario) {
		this.formulario = formulario;
		ouvintes = new ArrayList<>();
		addChangeListener(e -> abaSelecionada());
	}

	public void adicionarOuvinte(FicharioListener listener) {
		ouvintes.add(listener);
	}

	public void setRaiz(Objeto raiz) {
		this.raiz = raiz;
	}

	private void abaSelecionada() {
		int indice = getSelectedIndex();

		if (indice != -1) {
			Container container = (Container) getComponentAt(indice);
			ouvintes.forEach(o -> o.containerSelecionado(container));
		}
	}

	private void notificarContainerExcluido(Container container) {
		ouvintes.forEach(o -> o.containerExcluido(container));
	}

	public void addAba(String chaveTitulo, Objeto objeto, boolean clonar) throws Exception {
		objeto = objeto.clonar();

		ObjetoUtil.inflar(objeto);

		Container container = new Container(objeto);
		container.adicionarOuvinte(containerListener);
		addTab(chaveTitulo, container);

		Titulo titulo = new Titulo(this, clonar);
		titulo.adicionarOuvinte(tituloListener);
		setTabComponentAt(getTabCount() - 1, titulo);
	}

	private TituloListener tituloListener = new TituloListener() {
		@Override
		public void excluirAba(int indice) {
			Container container = (Container) getComponentAt(indice);
			notificarContainerExcluido(container);
			remove(indice);
		}

		@Override
		public void clonarAba() {
			try {
				addAba("label.objetos", raiz, false);
			} catch (Exception ex) {
				Util.stackTraceAndMessage("CLONAR ABA", ex, Fichario.this);
			}
		}
	};

	private ContainerListener containerListener = new ContainerListener() {
		@Override
		public void selecionadoObjeto(Container container) {
			ouvintes.forEach(o -> o.selecionadoObjeto(container));
		}

		@Override
		public void pedidoAtualizarObjeto(Container container) {
			ouvintes.forEach(o -> o.pedidoAtualizarObjeto(container));
		}

		@Override
		public void pedidoDestacarObjeto(Container container) {
			ouvintes.forEach(o -> o.pedidoDestacarObjeto(container));
		}

		@Override
		public void pedidoExcluirObjeto(Container container) {
			ouvintes.forEach(o -> o.pedidoExcluirObjeto(container));
		}
	};

	@Override
	public Component clonar() {
		Fichario fichario = new Fichario(formulario);
		fichario.adicionarOuvinte(formulario.getFicharioListener());

		try {
			fichario.addAba("label.objetos", raiz, true);
			fichario.raiz = raiz;
		} catch (Exception ex) {
			Util.stackTraceAndMessage("CRIAR ESPELHO", ex, formulario);
		}

		return fichario;
	}
}