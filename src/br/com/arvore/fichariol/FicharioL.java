package br.com.arvore.fichariol;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.comp.TabbedPane;
import br.com.arvore.containerl.ContainerL;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.formulario.Formulario;
import br.com.arvore.titulo.Titulo;
import br.com.arvore.titulo.TituloListener;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class FicharioL extends TabbedPane {
	private static final long serialVersionUID = 1L;
	private final List<FicharioLListener> ouvintes;
	private final Formulario formulario;

	public FicharioL(Formulario formulario) {
		addChangeListener(e -> abaSelecionada());
		this.formulario = formulario;
		ouvintes = new ArrayList<>();
	}

	public void adicionarOuvinte(FicharioLListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.add(listener);
	}

	public void excluirOuvinte(FicharioLListener listener) {
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
			ContainerL container = (ContainerL) getComponentAt(indice);
			ouvintes.forEach(o -> o.containerSelecionado(container));
		}
	}

	public void setDividerLocation(int indice) {
		ContainerL container = (ContainerL) getComponentAt(indice);
		container.setDividerLocation();
	}

	public void adicionarAba(boolean principal) throws Exception {
		ContainerL container = new ContainerL();
		addTab("label.layout", container);

		Fichario fichario = new Fichario(formulario.getRaiz());
		fichario.adicionarOuvinte(formulario.getFicharioListener());
		fichario.setSize(new Dimension(getWidth(), 0));
		fichario.adicionarAba(true);
		container.set(fichario);

		Titulo titulo = new Titulo(Mensagens.getString("label.layout"), principal);
		setTabComponentAt(getTabCount() - 1, titulo);
		titulo.adicionarOuvinte(tituloListener);

		setDividerLocation(getTabCount() - 1);
	}

	protected TituloListener tituloListener = new TituloListener() {
		// @Override
		// public void selecionarObjeto(Titulo titulo) {
		// abaSelecionada();
		// };

		@Override
		public void restaurar(Titulo titulo) {
		}

		@Override
		public void maximizar(Titulo titulo) {
		}

		@Override
		public void renomear(Titulo titulo) {
		}

		@Override
		public void clonarLocalDivisor(Titulo titulo) {
		}

		@Override
		public void excluirAba(Titulo titulo) {
			int indice = indexOfTabComponent(titulo);
			ContainerL container = (ContainerL) getComponentAt(indice);
			remove(indice);
			ouvintes.forEach(o -> o.containerExcluido(container));
		}

		@Override
		public void clonarAba(Titulo titulo) {
			try {
				adicionarAba(false);
			} catch (Exception ex) {
				Util.stackTraceAndMessage("CLONAR LAYOUT", ex, FicharioL.this);
			}
		}

		@Override
		public void excluirFichario(Titulo titulo) {
		}

		@Override
		public void cloneEsquerdo(Titulo titulo) {
		}

		@Override
		public void cloneDireito(Titulo titulo) {
		}

		@Override
		public void cloneAbaixo(Titulo titulo) {
		}

		@Override
		public void cloneAcima(Titulo titulo) {
		}
	};
}