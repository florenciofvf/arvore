package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.ArvoreListener;
import br.com.arvore.comp.PanelBorder;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.comp.SplitPane;
import br.com.arvore.comp.SplitPaneListener;
import br.com.arvore.comp.Table;
import br.com.arvore.modelo.ModeloOrdenacao;
import br.com.arvore.modelo.ModeloRegistro;
import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Util;

public class FicharioAba extends PanelBorder implements ArvoreListener, SplitPaneListener {
	private static final long serialVersionUID = 1L;
	private final SplitPane splitPane = new SplitPane();
	private final Table table = new Table();
	private final Popup popup = new Popup();
	private final Arvore arvore;
	private Objeto selecionado;

	public FicharioAba(Arvore arvore) {
		arvore.adicionarOuvinte(this);
		this.arvore = arvore;
		montarLayout();
		configurar();
	}

	public Arvore getArvore() {
		return arvore;
	}

	private void montarLayout() {
		splitPane.setListener(this);
		add(BorderLayout.CENTER, splitPane);
		splitPane.setLeftComponent(new ScrollPane(arvore));
		splitPane.setRightComponent(new ScrollPane(table));
		splitPane.setDividerLocation(Constantes.DIV_ARVORE_TABELA);
	}

	private void configurar() {
		popup.itemAtualizar.addActionListener(e -> atualizarArvore(selecionado));
	}

	void criarModeloRegistro(Objeto objeto) {
		try {
			ModeloRegistro modeloRegistro = ModeloRegistro.criarModelo(objeto);
			ModeloOrdenacao modeloOrdenacao = new ModeloOrdenacao(modeloRegistro, modeloRegistro.getColunasNumero());

			table.setModel(modeloOrdenacao);
			Util.ajustar(table, getGraphics());
		} catch (Exception ex) {
			Util.stackTraceAndMessage("REGISTROS", ex, this);
		}
	}

	void atualizarArvore(Objeto objeto) {
		try {
			ArvoreUtil.inflar(objeto);
			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("ARVORE", ex, this);
		}
	}

	@Override
	public void exibirPopup(Arvore arvore, Objeto selecionado, MouseEvent e) {
		this.selecionado = selecionado;
		popup.show(arvore, e.getX(), e.getY());
	}

	@Override
	public void clicado(Arvore arvore, Objeto objeto) {
		if (Constantes.INFLAR_DESATIVADO) {
			return;
		}

		if (objeto.isPesquisaPopup() || Util.estaVazio(objeto.getInstrucaoTabela())) {
			table.setModel(new ModeloOrdenacao());
			return;
		}

		criarModeloRegistro(objeto);
	}

	@Override
	public void localizacao(int i) {
		Constantes.DIV_ARVORE_TABELA = i;
	}
}