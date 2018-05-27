package br.com.arvore.formulario;

import java.awt.BorderLayout;

import br.com.arvore.Objeto;
import br.com.arvore.ObjetoUtil;
import br.com.arvore.componente.Button;
import br.com.arvore.componente.Label;
import br.com.arvore.componente.PanelBorder;
import br.com.arvore.componente.PanelLeft;
import br.com.arvore.componente.TabbedPane;
import br.com.arvore.componente.TextArea;
import br.com.arvore.container.Container;
import br.com.arvore.dialogo.DialogoObjeto;
import br.com.arvore.modelo.ModeloOrdenacao;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class Controle extends PanelBorder {
	private static final long serialVersionUID = 1L;
	private final String[][] MATRIZ = { { Mensagens.getString("label.arvore"), Mensagens.getString("label.atualizar") },
			{ Mensagens.getString("label.tabela"), Mensagens.getString("label.registros") },
			{ Mensagens.getString("label.update"), Mensagens.getString("label.update_dados") },
			{ Mensagens.getString("label.delete"), Mensagens.getString("label.delete_dados") },
			{ Mensagens.getString("label.insert"), Mensagens.getString("label.insert_dados") } };

	private final TextArea textAreaArvore = new TextArea();
	private final TextArea textAreaTabela = new TextArea();
	private final TextArea textAreaUpdate = new TextArea();
	private final TextArea textAreaDelete = new TextArea();
	private final TextArea textAreaInsert = new TextArea();
	private final TextArea textAreaObserv = new TextArea();
	private final TextArea textAreaDescri = new TextArea();
	private final TextArea textAreaComent = new TextArea();
	private final TextArea textAreaAlerta = new TextArea();

	private final TabbedPane fichario = new TabbedPane();
	private final Button buttonExecutar = new Button();
	private final Label labelStatus = new Label();
	private final Formulario formulario;
	private Container container;
	private int abaControleSel;

	private final byte ARVORE = 0;
	private final byte TABELA = 1;
	private final byte UPDATE = 2;
	private final byte DELETE = 3;
	private final byte INSERT = 4;

	public Controle(Formulario formulario) {
		this.formulario = formulario;
		montarLayout();
		configurar();
	}

	private void configurar() {
		buttonExecutar.addActionListener(e -> processar());
		fichario.addChangeListener(e -> controleButton());
	}

	private void montarLayout() {
		add(BorderLayout.NORTH, labelStatus);
		add(BorderLayout.CENTER, fichario);
		add(BorderLayout.SOUTH, new PanelLeft(buttonExecutar));
	}

	private void controleButton() {
		String hint = getHintButton();
		buttonExecutar.setToolTipText(hint);
		buttonExecutar.setVisible(hint != null);

		if (buttonExecutar.isVisible()) {
			int indice = ControleUtil.getIndiceAbaAtiva(fichario);

			if (indice != -1) {
				buttonExecutar.setIcon(fichario.getIconAt(indice));
			}
		}
	}

	private String getHintButton() {
		String tituloAtivo = ControleUtil.getTituloAbaAtiva(fichario);

		for (String[] linha : MATRIZ) {
			if (linha[0].equals(tituloAtivo)) {
				return linha[1];
			}
		}

		return null;
	}

	private boolean ehAbaAtiva(int linha) {
		String tituloAtivo = ControleUtil.getTituloAbaAtiva(fichario);
		return MATRIZ[linha][0].equals(tituloAtivo);
	}

	private void processar() {
		if (ehAbaAtiva(ARVORE)) {
			arvore();
		} else if (ehAbaAtiva(TABELA)) {
			tabela();
		} else if (ehAbaAtiva(INSERT)) {
			insert();
		} else if (ehAbaAtiva(UPDATE)) {
			update();
		} else if (ehAbaAtiva(DELETE)) {
			delete();
		}
	}

	private void arvore() {
		final Objeto selecionado = ControleUtil.getObjetoSelecionado(container);

		if (selecionado == null) {
			return;
		}

		if (textAreaArvore.estaVazio()) {
			Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_arvore"));
			return;
		}

		selecionado.setInstrucaoArvore(textAreaArvore.getText());

		try {
			container.getArvore().inflarSelecionado();
		} catch (Exception ex) {
			Util.stackTraceAndMessage("ATUALIZAR OBJETO", ex, formulario);
		}
	}

	private void tabela() {
		final Objeto selecionado = ControleUtil.getObjetoSelecionado(container);

		if (selecionado == null) {
			return;
		}

		if (textAreaTabela.estaVazio()) {
			Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_tabela"));
			return;
		}

		selecionado.setInstrucaoTabela(textAreaTabela.getText());

		try {
			container.exibirRegistros(selecionado);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("EXIBIR REGISTROS", ex, formulario);
		}
	}

	private void insert() {
		final Objeto selecionado = ControleUtil.getObjetoSelecionado(container);

		if (selecionado == null) {
			return;
		}

		if (textAreaInsert.estaVazio()) {
			Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_insert"));
			return;
		}

		selecionado.setInstrucaoInsert(textAreaInsert.getText());

		try {
			int i = ObjetoUtil.inserirObjeto(selecionado);
			Util.mensagem(formulario, Mensagens.getString("label.inseridos") + " (" + i + ")");
			container.getArvore().inflarSelecionado();
		} catch (Exception ex) {
			Util.stackTraceAndMessage("INSERT OBJETO", ex, formulario);
		}
	}

	private void update() {
		final Objeto selecionado = ControleUtil.getObjetoSelecionado(container);

		if (selecionado == null) {
			return;
		}

		if (textAreaUpdate.estaVazio()) {
			Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_update"));
			return;
		}

		if (!Util.confirmaAtualizacao(formulario)) {
			return;
		}

		selecionado.setInstrucaoUpdate(textAreaUpdate.getText());

		try {
			int i = ObjetoUtil.atualizarObjetos(selecionado);
			Util.mensagem(formulario, Mensagens.getString("label.atualizados") + " (" + i + ")");
			container.getArvore().inflarSelecionado();
		} catch (Exception ex) {
			Util.stackTraceAndMessage("UPDATE OBJETO", ex, formulario);
		}
	}

	private void delete() {
		final Objeto selecionado = ControleUtil.getObjetoSelecionado(container);

		if (selecionado == null) {
			return;
		}

		if (textAreaDelete.estaVazio()) {
			Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_delete"));
			return;
		}

		if (!Util.confirmaExclusao(formulario)) {
			return;
		}

		selecionado.setInstrucaoDelete(textAreaDelete.getText());

		try {
			int i = ObjetoUtil.excluirObjetos(selecionado);
			Util.mensagem(formulario, Mensagens.getString("label.excluidos") + " (" + i + ")");
			container.getArvore().excluirSelecionado();
			selecionadoObjeto(container);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("DELETE OBJETO", ex, formulario);
		}
	}

	public void acaoObjeto(Container container, String chaveTitulo) {
		selecionadoObjeto(container);

		int indice = fichario.indexOfTab(Mensagens.getString(chaveTitulo));

		if (indice != -1) {
			fichario.setSelectedIndex(indice);
			processar();
		}
	}

	public void destacarObjeto(Container container) {
		final Objeto selecionado = ControleUtil.getObjetoSelecionado(container);

		if (selecionado == null) {
			return;
		}

		new DialogoObjeto(formulario, selecionado);
	}

	public void containerExcluido(Container container) {
		if (this.container == container) {
			selecionadoObjeto(null);
		}
	}

	public void selecionadoObjeto(Container container) {
		final Objeto selecionado = ControleUtil.getObjetoSelecionado(container);
		final int abaSelecionada = ControleUtil.getIndiceAbaAtiva(fichario);
		final int controleSelTmp = abaControleSel;

		labelStatus.setIcon(null);
		labelStatus.setText("");
		fichario.removeAll();

		if (selecionado == null) {
			return;
		}

		if (selecionado.estaVazio() && selecionado.isManterVazio()) {
			labelStatus.setIcon(selecionado.getIconeManterVazio());
		} else {
			labelStatus.setIcon(selecionado.getIcone());
		}

		labelStatus.setText(selecionado.getTitulo());
		this.container = container;

		textAreaArvore.setText(Util.normalizar(selecionado.getInstrucaoArvore()));
		textAreaTabela.setText(Util.normalizar(selecionado.getInstrucaoTabela()));
		textAreaUpdate.setText(Util.normalizar(selecionado.getInstrucaoUpdate()));
		textAreaDelete.setText(Util.normalizar(selecionado.getInstrucaoDelete()));
		textAreaInsert.setText(Util.normalizar(selecionado.getInstrucaoInsert()));

		textAreaObserv.setText(Util.normalizar(selecionado.getObservacao()));
		textAreaComent.setText(Util.normalizar(selecionado.getComentario()));
		textAreaDescri.setText(Util.normalizar(selecionado.getDescricao()));
		textAreaAlerta.setText(Util.normalizar(selecionado.getAlerta()));

		abaControleSel = 0;

		if (!textAreaArvore.estaVazio()) {
			fichario.addTab("label.arvore", Icones.ARVORE, textAreaArvore);
			abaControleSel += 1;
		}

		if (!textAreaTabela.estaVazio()) {
			fichario.addTab("label.tabela", Icones.TABELA, textAreaTabela);
			abaControleSel += 2;
		}

		if (!textAreaUpdate.estaVazio()) {
			fichario.addTab("label.update", Icones.UPDATE, textAreaUpdate);
			abaControleSel += 4;
		}

		if (!textAreaDelete.estaVazio()) {
			fichario.addTab("label.delete", Icones.EXCLUIR, textAreaDelete);
			abaControleSel += 8;
		}

		if (!textAreaInsert.estaVazio()) {
			fichario.addTab("label.insert", Icones.CRIAR, textAreaInsert);
			abaControleSel += 16;
		}

		if (!textAreaObserv.estaVazio()) {
			fichario.addTab("label.observacao", Icones.OBSERVACAO, textAreaObserv);
			textAreaObserv.insert(0, " ");
			abaControleSel += 32;
		}

		if (!textAreaDescri.estaVazio()) {
			fichario.addTab("label.descricao", Icones.INFO, textAreaDescri);
			textAreaDescri.insert(0, " ");
			abaControleSel += 64;
		}

		if (!textAreaComent.estaVazio()) {
			fichario.addTab("label.comentario", Icones.COMENTARIO, textAreaComent);
			textAreaComent.insert(0, " ");
			abaControleSel += 128;
		}

		if (!textAreaAlerta.estaVazio()) {
			fichario.addTab("label.alerta", Icones.ALERTA, textAreaAlerta);
			textAreaAlerta.insert(0, " ");
			abaControleSel += 256;
		}

		controleButton();
		selecionarAba(abaSelecionada, controleSelTmp);

		if (Constantes.INFLAR_DESATIVADO) {
			return;
		}

		if (selecionado.isPesquisaPopup() || textAreaTabela.estaVazio()) {
			container.getTabela().setModel(new ModeloOrdenacao());
			return;
		}

		tabela();
	}

	private void selecionarAba(int indice, int controle) {
		if (indice > 0 && indice < fichario.getTabCount() && controle == abaControleSel) {
			fichario.setSelectedIndex(indice);
		}
	}
}