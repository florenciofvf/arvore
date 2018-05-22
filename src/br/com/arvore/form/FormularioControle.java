package br.com.arvore.form;

import java.awt.BorderLayout;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.Button;
import br.com.arvore.comp.Label;
import br.com.arvore.comp.PanelBorder;
import br.com.arvore.comp.PanelLeft;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.comp.TextArea;
import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class FormularioControle extends PanelBorder {
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
	private final byte ARVORE = 0;
	private final byte TABELA = 1;
	private final byte UPDATE = 2;
	private final byte DELETE = 3;
	private final byte INSERT = 4;
	private int abaControleSel;
	private Arvore arvore;
	private Objeto objeto;

	public FormularioControle(Formulario formulario) {
		this.formulario = formulario;
		montarLayout();
		configurar();
	}

	private void configurar() {
		buttonExecutar.addActionListener(e -> processar());

		fichario.addChangeListener(e -> controleButton());
	}

	public int getAbaSelecionada() {
		return fichario.getSelectedIndex();
	}

	public int getAbaControleSel() {
		return abaControleSel;
	}

	public void selecionarAba(int i, int controle) {
		if (i > 0 && i < fichario.getTabCount() && controle == abaControleSel) {
			fichario.setSelectedIndex(i);
		}
	}

	private void montarLayout() {
		add(BorderLayout.NORTH, labelStatus);
		add(BorderLayout.CENTER, fichario);
		add(BorderLayout.SOUTH, new PanelLeft(buttonExecutar));
	}

	private void controleButton() {
		String hint = getHint();
		buttonExecutar.setToolTipText(hint);
		buttonExecutar.setVisible(hint != null);

		if (buttonExecutar.isVisible()) {
			int i = fichario.getSelectedIndex();

			if (i != -1) {
				buttonExecutar.setIcon(fichario.getIconAt(i));
			}
		}
	}

	private String getTituloAbaAtiva() {
		int i = fichario.getSelectedIndex();

		if (i == -1) {
			return null;
		}

		return fichario.getTitleAt(i);
	}

	private String getHint() {
		String tituloAtivo = getTituloAbaAtiva();

		for (String[] linha : MATRIZ) {
			if (linha[0].equals(tituloAtivo)) {
				return linha[1];
			}
		}

		return null;
	}

	private boolean ehAbaAtiva(int indice) {
		String tituloAtivo = getTituloAbaAtiva();
		return MATRIZ[indice][0].equals(tituloAtivo);
	}

	private void processar() {
		if (ehAbaAtiva(ARVORE)) {
			if (!textAreaArvore.estaVazio()) {
				objeto.setInstrucaoArvore(textAreaArvore.getText());
			}
			formulario.atualizarArvore(objeto);

		} else if (ehAbaAtiva(TABELA)) {

			objeto.setInstrucaoTabela(textAreaTabela.getText());
			if (textAreaTabela.estaVazio()) {
				Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_tabela"));
				return;
			}
			formulario.criarModeloRegistro(objeto);

		} else if (ehAbaAtiva(UPDATE)) {

			objeto.setInstrucaoUpdate(textAreaUpdate.getText());
			if (textAreaUpdate.estaVazio()) {
				Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_update"));
				return;
			}
			atualizar(objeto);

		} else if (ehAbaAtiva(DELETE)) {

			objeto.setInstrucaoDelete(textAreaDelete.getText());
			if (textAreaDelete.estaVazio()) {
				Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_delete"));
				return;
			}
			excluir(objeto);

		} else if (ehAbaAtiva(INSERT)) {

			objeto.setInstrucaoInsert(textAreaInsert.getText());
			if (textAreaInsert.estaVazio()) {
				Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_insert"));
				return;
			}
			inserir(objeto);

		}
	}

	private void inserir(Objeto objeto) {
		try {
			int i = ArvoreUtil.inserirObjeto(objeto);
			Util.mensagem(formulario, Mensagens.getString("label.sucesso") + " (" + i + ")");

			ArvoreUtil.inflar(objeto);
			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("INSERIR", ex, formulario);
		}
	}

	private void excluir(Objeto objeto) {
		if (!Util.confirmaExclusao(formulario)) {
			return;
		}

		try {
			int i = ArvoreUtil.excluirObjetos(objeto);
			Util.mensagem(formulario, Mensagens.getString("label.sucesso") + " (" + i + ")");

			ArvoreUtil.inflar(objeto);
			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("EXCLUIR", ex, formulario);
		}
	}

	private void atualizar(Objeto objeto) {
		if (!Util.confirmaAtualizacao(formulario)) {
			return;
		}

		try {
			int i = ArvoreUtil.atualizarObjetos(objeto);
			Util.mensagem(formulario, Mensagens.getString("label.sucesso") + " (" + i + ")");

			ArvoreUtil.inflar(objeto);
			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("ATUALIZAR", ex, formulario);
		}
	}

	public void clicado(Arvore arvore, Objeto objeto) {
		if (objeto.estaVazio() && objeto.isManterVazio()) {
			labelStatus.setIcon(objeto.getIconeManterVazio());
		} else {
			labelStatus.setIcon(objeto.getIcone());
		}

		labelStatus.setText(arvore == null ? "" : objeto.getTitulo());
		this.arvore = arvore;
		this.objeto = objeto;
		fichario.removeAll();

		abaControleSel = 0;

		textAreaArvore.setText(Util.normalizar(objeto.getInstrucaoArvore()));
		textAreaTabela.setText(Util.normalizar(objeto.getInstrucaoTabela()));
		textAreaUpdate.setText(Util.normalizar(objeto.getInstrucaoUpdate()));
		textAreaDelete.setText(Util.normalizar(objeto.getInstrucaoDelete()));
		textAreaInsert.setText(Util.normalizar(objeto.getInstrucaoInsert()));
		textAreaObserv.setText(Util.normalizar(objeto.getObservacao()));
		textAreaComent.setText(Util.normalizar(objeto.getComentario()));
		textAreaDescri.setText(Util.normalizar(objeto.getDescricao()));
		textAreaAlerta.setText(Util.normalizar(objeto.getAlerta()));

		if (!Util.estaVazio(textAreaArvore.getText())) {
			fichario.addTab("label.arvore", Icones.ARVORE, textAreaArvore);
			abaControleSel += 1;
		}

		if (!Util.estaVazio(textAreaTabela.getText())) {
			fichario.addTab("label.tabela", Icones.TABELA, textAreaTabela);
			abaControleSel += 2;
		}

		if (!Util.estaVazio(textAreaUpdate.getText())) {
			fichario.addTab("label.update", Icones.UPDATE, textAreaUpdate);
			abaControleSel += 4;
		}

		if (!Util.estaVazio(textAreaDelete.getText())) {
			fichario.addTab("label.delete", Icones.EXCLUIR, textAreaDelete);
			abaControleSel += 8;
		}

		if (!Util.estaVazio(textAreaInsert.getText())) {
			fichario.addTab("label.insert", Icones.CRIAR, textAreaInsert);
			abaControleSel += 16;
		}

		if (!Util.estaVazio(textAreaObserv.getText())) {
			fichario.addTab("label.observacao", Icones.OBSERVACAO, textAreaObserv);
			abaControleSel += 32;
		}

		if (!Util.estaVazio(textAreaDescri.getText())) {
			fichario.addTab("label.descricao", Icones.INFO, textAreaDescri);
			abaControleSel += 64;
		}

		if (!Util.estaVazio(textAreaComent.getText())) {
			fichario.addTab("label.comentario", Icones.COMENTARIO, textAreaComent);
			abaControleSel += 128;
		}

		if (!Util.estaVazio(textAreaAlerta.getText())) {
			fichario.addTab("label.alerta", Icones.ALERTA, textAreaAlerta);
			abaControleSel += 256;
		}

		controleButton();
	}

	public Arvore getArvore() {
		return arvore;
	}
}