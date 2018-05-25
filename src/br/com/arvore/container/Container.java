package br.com.arvore.container;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.arvore.Arvore;
import br.com.arvore.arvore.ArvoreListener;
import br.com.arvore.comp.PanelBorder;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.comp.SplitPane;
import br.com.arvore.comp.SplitPaneListener;
import br.com.arvore.tabela.Tabela;
import br.com.arvore.util.Constantes;

public class Container extends PanelBorder {
	private static final long serialVersionUID = 1L;
	private final SplitPane splitPane = new SplitPane();
	private final List<ContainerListener> ouvintes;
	private final Tabela tabela = new Tabela();
	private final Arvore arvore;

	public Container(Arvore arvore) {
		arvore.limparOuvintes();
		arvore.adicionarOuvinte(arvoreListener);
		ouvintes = new ArrayList<>();
		this.arvore = arvore;
		montarLayout();
	}

	public void adicionarOuvinte(ContainerListener listener) {
		ouvintes.add(listener);
	}

	public void limparOuvintes() {
		ouvintes.clear();
	}

	private void notificarSelecionado() {
		for (ContainerListener ouvinte : ouvintes) {
			ouvinte.selecionado(this);
		}
	}

	private void notificarAtualizar() {
		for (ContainerListener ouvinte : ouvintes) {
			ouvinte.pedidoAtualizar(this);
		}
	}

	private void notificarDestacar() {
		for (ContainerListener ouvinte : ouvintes) {
			ouvinte.pedidoDestacar(this);
		}
	}

	private void notificarExcluir() {
		for (ContainerListener ouvinte : ouvintes) {
			ouvinte.pedidoExcluir(this);
		}
	}

	private void montarLayout() {
		add(BorderLayout.CENTER, splitPane);
		splitPane.setListener(splitPaneListener);
		splitPane.setLeftComponent(new ScrollPane(arvore));
		splitPane.setRightComponent(new ScrollPane(tabela));
		splitPane.setDividerLocation(Constantes.DIV_ARVORE_TABELA);
	}

	private ArvoreListener arvoreListener = new ArvoreListener() {
		private void checar(Arvore arvore) {
			if (Container.this.arvore != arvore) {
				throw new IllegalStateException();
			}
		}

		@Override
		public void selecionado(Arvore arvore) {
			checar(arvore);
			notificarSelecionado();
		}

		@Override
		public void pedidoExcluir(Arvore arvore) {
			checar(arvore);
			notificarExcluir();
		}

		@Override
		public void pedidoDestacar(Arvore arvore) {
			checar(arvore);
			notificarDestacar();
		}

		@Override
		public void pedidoAtualizar(Arvore arvore) {
			checar(arvore);
			notificarAtualizar();
		}
	};

	private SplitPaneListener splitPaneListener = new SplitPaneListener() {
		@Override
		public void localizacao(int i) {
			Constantes.DIV_ARVORE_TABELA = i;
		}
	};
}