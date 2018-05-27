package br.com.arvore.container;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.Objeto;
import br.com.arvore.arvore.Arvore;
import br.com.arvore.arvore.ArvoreListener;
import br.com.arvore.componente.PanelBorder;
import br.com.arvore.componente.ScrollPane;
import br.com.arvore.componente.SplitPane;
import br.com.arvore.componente.SplitPaneListener;
import br.com.arvore.modelo.ModeloArvore;
import br.com.arvore.modelo.ModeloOrdenacao;
import br.com.arvore.modelo.ModeloRegistro;
import br.com.arvore.tabela.Tabela;
import br.com.arvore.tabela.TabelaUtil;
import br.com.arvore.util.Constantes;

public class Container extends PanelBorder {
	private static final long serialVersionUID = 1L;
	private final SplitPane splitPane = new SplitPane();
	private final List<ContainerListener> ouvintes;
	private final Tabela tabela = new Tabela();
	private final Arvore arvore;

	public Container(Objeto objeto) {
		arvore = new Arvore(new ModeloArvore(objeto));
		arvore.adicionarOuvinte(arvoreListener);
		ouvintes = new ArrayList<>();
		montarLayout();
	}

	public void adicionarOuvinte(ContainerListener listener) {
		ouvintes.add(listener);
	}

	public Arvore getArvore() {
		return arvore;
	}

	public Tabela getTabela() {
		return tabela;
	}

	private void montarLayout() {
		add(BorderLayout.CENTER, splitPane);
		splitPane.setListener(splitPaneListener);
		splitPane.setLeftComponent(new ScrollPane(arvore));
		splitPane.setRightComponent(new ScrollPane(tabela));
		splitPane.setDividerLocation(Constantes.DIV_ARVORE_TABELA);
	}

	public void exibirRegistros(Objeto objeto) throws Exception {
		ModeloRegistro modeloRegistro = ModeloRegistro.criarModelo(objeto);
		ModeloOrdenacao modeloOrdenacao = new ModeloOrdenacao(modeloRegistro, modeloRegistro.getColunasNumero());
		tabela.limparOuvintes();
		tabela.adicionarOuvinte(modeloOrdenacao);
		tabela.setModel(modeloOrdenacao);
		TabelaUtil.ajustar(tabela, getGraphics());
	}

	private ArvoreListener arvoreListener = new ArvoreListener() {
		private void checar(Arvore arvore) {
			if (Container.this.arvore != arvore) {
				throw new IllegalStateException();
			}
		}

		@Override
		public void selecionadoObjeto(Arvore arvore) {
			checar(arvore);
			ouvintes.forEach(o -> o.selecionadoObjeto(Container.this));
		}

		@Override
		public void pedidoExcluirObjeto(Arvore arvore) {
			checar(arvore);
			ouvintes.forEach(o -> o.pedidoExcluirObjeto(Container.this));
		}

		@Override
		public void pedidoDestacarObjeto(Arvore arvore) {
			checar(arvore);
			ouvintes.forEach(o -> o.pedidoDestacarObjeto(Container.this));
		}

		@Override
		public void pedidoAtualizarObjeto(Arvore arvore) {
			checar(arvore);
			ouvintes.forEach(o -> o.pedidoAtualizarObjeto(Container.this));
		}
	};

	private SplitPaneListener splitPaneListener = new SplitPaneListener() {
		@Override
		public void localizacao(int i) {
			Constantes.DIV_ARVORE_TABELA = i;
		}
	};
}