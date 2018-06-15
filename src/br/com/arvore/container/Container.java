package br.com.arvore.container;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import br.com.arvore.Objeto;
import br.com.arvore.arvore.Arvore;
import br.com.arvore.arvore.ArvoreListener;
import br.com.arvore.comp.PanelBorder;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.comp.SplitPane;
import br.com.arvore.modelo.ModeloArvore;
import br.com.arvore.modelo.ModeloOrdenacao;
import br.com.arvore.modelo.ModeloRegistro;
import br.com.arvore.tabela.Tabela;
import br.com.arvore.tabela.TabelaUtil;
import br.com.arvore.util.Layout;
import br.com.arvore.util.Obj;
import br.com.arvore.util.Util;
import br.com.arvore.util.XMLUtil;

public class Container extends PanelBorder implements Layout {
	private static final long serialVersionUID = 1L;
	private final SplitPane splitPane = new SplitPane();
	private final List<ContainerListener> ouvintes;
	private final Tabela tabela = new Tabela();
	private final Arvore arvore;
	private boolean maximizado;
	private String titulo;
	private int[] indices;

	public Container(Objeto objeto) {
		arvore = new Arvore(new ModeloArvore(objeto));
		arvore.adicionarOuvinte(arvoreListener);
		ouvintes = new ArrayList<>();
		montarLayout();
	}

	public boolean isMaximizado() {
		return maximizado;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void adicionarOuvinte(ContainerListener listener) {
		ouvintes.add(listener);
	}

	public Objeto getObjetoSelecionado() {
		return arvore.getObjetoSelecionado();
	}

	public void inflarSelecionado() throws Exception {
		arvore.inflarSelecionado();
	}

	public void excluirSelecionado() {
		arvore.excluirSelecionado();
	}

	private void montarLayout() {
		splitPane.setRightComponent(new ScrollPane(tabela));
		splitPane.setLeftComponent(new ScrollPane(arvore));
		add(BorderLayout.CENTER, splitPane);
	}

	public int getDividerLocation() {
		return splitPane.getDividerLocation();
	}

	public void setDividerLocation(int i) {
		splitPane.setDividerLocation(i);
	}

	public void maximizar() {
		if (maximizado) {
			return;
		}

		removeAll();
		maximizado = true;
		add(BorderLayout.CENTER, new ScrollPane(arvore));
		SwingUtilities.updateComponentTreeUI(this);
	}

	public void restaurar() {
		if (!maximizado) {
			return;
		}

		removeAll();
		maximizado = false;
		splitPane.setLeftComponent(new ScrollPane(arvore));
		add(BorderLayout.CENTER, splitPane);
		setDividerLocation(getWidth() / 2);
		SwingUtilities.updateComponentTreeUI(this);
	}

	public void setModeloOrdenacao(ModeloOrdenacao modeloOrdenacao) {
		if (maximizado || modeloOrdenacao == null) {
			return;
		}

		tabela.setModel(modeloOrdenacao);
	}

	public void exibirRegistros(Objeto objeto) throws Exception {
		if (maximizado || objeto == null) {
			return;
		}

		ModeloRegistro modeloRegistro = ModeloRegistro.criarModelo(objeto);
		ModeloOrdenacao modeloOrdenacao = new ModeloOrdenacao(modeloRegistro, modeloRegistro.getColunasNumero());
		tabela.mementoSelecao();
		tabela.limparOuvintes();
		tabela.adicionarOuvinte(modeloOrdenacao);
		tabela.setModel(modeloOrdenacao);
		tabela.restaurarMemento();
		tabela.restaurarSelecao();
		TabelaUtil.ajustar(tabela, getGraphics());
	}

	private ArvoreListener arvoreListener = new ArvoreListener() {
		@Override
		public void selecionadoObjeto(Arvore arvore) {
			ouvintes.forEach(o -> o.selecionadoObjeto(Container.this));
		}

		@Override
		public void pedidoExcluirObjeto(Arvore arvore) {
			ouvintes.forEach(o -> o.pedidoExcluirObjeto(Container.this));
		}

		@Override
		public void pedidoDestacarObjeto(Arvore arvore) {
			ouvintes.forEach(o -> o.pedidoDestacarObjeto(Container.this));
		}

		@Override
		public void pedidoAtualizarObjeto(Arvore arvore) {
			ouvintes.forEach(o -> o.pedidoAtualizarObjeto(Container.this));
		}
	};

	@Override
	public void salvarLayout(XMLUtil xml) {
		ContainerUtil.salvarLayout(xml, this);
	}

	@Override
	public void aplicarLayout(Obj obj) {
		ContainerUtil.aplicarLayout(obj, this);
	}

	@Override
	public void ajusteScroll() {
		if (indices == null) {
			return;
		}

		Objeto raiz = (Objeto) arvore.getModel().getRoot();
		Objeto objeto = raiz.getObjeto(0, indices);
		arvore.selecionarObjeto(objeto);
	}

	public void inflarHierarquia(int[] indices) {
		if (indices == null) {
			return;
		}

		Objeto raiz = (Objeto) arvore.getModel().getRoot();

		try {
			raiz.inflarParcial(0, indices);
			this.indices = indices;
		} catch (Exception ex) {
			Util.stackTraceAndMessage("APLICAR LAYOUT", ex, this);
		}
	}
}