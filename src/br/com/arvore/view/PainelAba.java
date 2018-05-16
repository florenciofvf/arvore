package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.ArvoreListener;
import br.com.arvore.comp.PanelBorder;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.comp.SplitPane;
import br.com.arvore.comp.Table;
import br.com.arvore.modelo.ModeloOrdenacao;
import br.com.arvore.modelo.ModeloRegistro;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Util;

public class PainelAba extends PanelBorder implements ArvoreListener {
	private static final long serialVersionUID = 1L;
	private final SplitPane split = new SplitPane();
	private final Table table = new Table();
	private final Popup popup = new Popup();
	private final Formulario formulario;
	private final Arvore arvore;
	private Objeto selecionado;

	public PainelAba(Formulario formulario, Arvore arvore) {
		arvore.setArvoreListener(this);
		this.formulario = formulario;
		this.arvore = arvore;
		montarLayout();
		configurar();
	}

	private void montarLayout() {
		add(BorderLayout.CENTER, split);
		split.setLeftComponent(new ScrollPane(arvore));
		split.setRightComponent(new ScrollPane(table));
		split.setDividerLocation(Constantes.LAR_SPLIT);
	}

	private void configurar() {
		popup.itemPropriedades.addActionListener(e -> new ObjetoDialogo(formulario, this, arvore, selecionado));
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

	@Override
	public void exibirPopup(Arvore arvore, Objeto selecionado, MouseEvent e) {
		boolean instrucaoArvore = !Util.estaVazio(selecionado.getInstrucaoArvore());
		boolean instrucaoTabela = !Util.estaVazio(selecionado.getInstrucaoTabela());
		boolean instrucaoUpdate = !Util.estaVazio(selecionado.getInstrucaoUpdate());
		boolean instrucaoDelete = !Util.estaVazio(selecionado.getInstrucaoDelete());

		if (instrucaoArvore || instrucaoTabela || instrucaoUpdate || instrucaoDelete) {
			this.selecionado = selecionado;
			popup.show(arvore, e.getX(), e.getY());
		}
	}

	@Override
	public void clicado(Objeto objeto) {
		if (objeto.isPesquisaPopup() || Util.estaVazio(objeto.getInstrucaoTabela())) {
			return;
		}

		criarModeloRegistro(objeto);
	}
}