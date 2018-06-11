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
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Layout;
import br.com.arvore.util.Obj;
import br.com.arvore.util.XMLUtil;

public class Container extends PanelBorder implements Layout {
	private static final long serialVersionUID = 1L;
	private final SplitPane splitPane = new SplitPane();
	private final List<ContainerListener> ouvintes;
	private final Tabela tabela = new Tabela();
	private final Arvore arvore;
	private boolean maximizado;
	private String titulo;

	public Container(Objeto objeto) {
		arvore = new Arvore(new ModeloArvore(objeto));
		arvore.adicionarOuvinte(arvoreListener);
		ouvintes = new ArrayList<>();
		montarLayout();
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

	public Arvore getArvore() {
		return arvore;
	}

	public Tabela getTabela() {
		return tabela;
	}

	private void montarLayout() {
		splitPane.setRightComponent(new ScrollPane(tabela));
		splitPane.setLeftComponent(new ScrollPane(arvore));
		add(BorderLayout.CENTER, splitPane);
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

	public void exibirRegistros(Objeto objeto) throws Exception {
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

	@Override
	public void salvarLayout(XMLUtil xml) {
		xml.abrirTag(Constantes.CONTAINER);
		xml.atributo(Constantes.LOCAL_DIV, splitPane.getDividerLocation());
		xml.atributo(Constantes.TITULO, titulo);
		xml.fecharTag();
		xml.finalizarTag(Constantes.CONTAINER);
	}

	@Override
	public void aplicarLayout(Obj obj) {
		if (obj.isContainer()) {
			String localDiv = obj.getValorAtributo(Constantes.LOCAL_DIV);
			int local = Integer.parseInt(localDiv);
			setDividerLocation(local);
		} else {
			throw new IllegalStateException();
		}
	}
}