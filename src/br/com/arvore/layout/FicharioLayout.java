package br.com.arvore.layout;

import java.util.ArrayList;
import java.util.List;

import br.com.arvore.Objeto;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.container.Container;
import br.com.arvore.formulario.Formulario;

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

	private void abaSelecionada() {
//		int indice = getSelectedIndex();
//
//		if (indice != -1) {
//			Container container = (Container) getComponentAt(indice);
//			ouvintes.forEach(o -> o.containerSelecionado(container));
//		}
	}

	private void notificarContainerExcluido(Container container) {
//		ouvintes.forEach(o -> o.containerExcluido(container));
	}

	public void addAba(String chaveTitulo, Objeto objeto, boolean clonar) throws Exception {
//		objeto = objeto.clonar();
//
//		ObjetoUtil.inflar(objeto);
//
//		Container container = new Container(objeto);
//		container.adicionarOuvinte(containerListener);
//		addTab(chaveTitulo, container);
//
//		Titulo titulo = new Titulo(this, clonar);
//		titulo.adicionarOuvinte(tituloListener);
//		setTabComponentAt(getTabCount() - 1, titulo);
//
//		setDividerLocation(getTabCount() - 1);
	}

	private TituloLayoutListener tituloListener = new TituloLayoutListener() {
		@Override
		public void excluirAba(int indice) {
//			Container container = (Container) getComponentAt(indice);
//			notificarContainerExcluido(container);
			remove(indice);
		}

		@Override
		public void clonarAba() {
//			try {
//				addAba("label.objetos", raiz, false);
//			} catch (Exception ex) {
//				Util.stackTraceAndMessage("CLONAR ABA", ex, FicharioLayout.this);
//			}
		}
	};
}