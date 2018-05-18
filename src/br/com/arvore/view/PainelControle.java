package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.ArvoreListener;
import br.com.arvore.comp.Button;
import br.com.arvore.comp.Label;
import br.com.arvore.comp.PanelBorder;
import br.com.arvore.comp.PanelLeft;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.comp.TextArea;
import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class PainelControle extends PanelBorder implements ArvoreListener {
	private static final long serialVersionUID = 1L;
	private final TextArea textAreaArvore = new TextArea();
	private final TextArea textAreaTabela = new TextArea();
	private final TextArea textAreaUpdate = new TextArea();
	private final TextArea textAreaDelete = new TextArea();
	private final TextArea textAreaInsert = new TextArea();
	private final TextArea textAreaObserv = new TextArea();
	private final TextArea textAreaDescri = new TextArea();
	private final TabbedPane fichario = new TabbedPane();
	private final Button buttonExecutar = new Button();
	private final Label labelStatus = new Label();
	private final Formulario formulario;
	private Arvore arvore;
	private Objeto objeto;

	public PainelControle(Formulario formulario) {
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
		String s = getTituloSelecionado();
		buttonExecutar.setVisible(ehValido(s));

		if (buttonExecutar.isVisible()) {
			int i = fichario.getSelectedIndex();

			if (i != -1) {
				buttonExecutar.setIcon(fichario.getIconAt(i));
			}
		}
	}

	private boolean ehValido(String s) {
		String[] strings = { "label.arvore", "label.tabela", "label.update", "label.delete", "label.insert" };

		for (String string : strings) {
			if (ehTitulo(string, s)) {
				return true;
			}
		}

		return false;
	}

	private void processar() {
		String tituloSel = getTituloSelecionado();

		if (ehTitulo("label.arvore", tituloSel)) {
			if (!textAreaArvore.estaVazio()) {
				objeto.setInstrucaoArvore(textAreaArvore.getText());
			}
			formulario.getFichario().atualizarArvore(objeto);

		} else if (ehTitulo("label.tabela", tituloSel)) {

			objeto.setInstrucaoTabela(textAreaTabela.getText());
			if (textAreaTabela.estaVazio()) {
				Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_tabela"));
				return;
			}
			formulario.getFichario().criarModeloRegistro(objeto);

		} else if (ehTitulo("label.update", tituloSel)) {

			objeto.setInstrucaoUpdate(textAreaUpdate.getText());
			if (textAreaUpdate.estaVazio()) {
				Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_update"));
				return;
			}
			atualizar(objeto);

		} else if (ehTitulo("label.delete", tituloSel)) {

			objeto.setInstrucaoDelete(textAreaDelete.getText());
			if (textAreaDelete.estaVazio()) {
				Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_delete"));
				return;
			}
			excluir(objeto);

		} else if (ehTitulo("label.insert", tituloSel)) {

			objeto.setInstrucaoInsert(textAreaDelete.getText());
			if (textAreaDelete.estaVazio()) {
				Util.mensagem(formulario, Mensagens.getString("erro.sem_instrucao_insert"));
				return;
			}
			inserir(objeto);

		}
	}

	private void inserir(Objeto objeto) {
		try {
			int i = ArvoreUtil.inserirObjeto(objeto);
			Util.mensagem(this, Mensagens.getString("label.sucesso") + " (" + i + ")");

			if (Constantes.INFLAR_ANTECIPADO) {
				objeto.inflar();
			} else {
				objeto.inflarParcial2();
			}

			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("Excluir", ex, formulario);
		}
	}

	private void excluir(Objeto objeto) {
		if (!Util.confirmaExclusao(formulario)) {
			return;
		}

		try {
			int i = ArvoreUtil.excluirObjetos(objeto);
			Util.mensagem(formulario, Mensagens.getString("label.sucesso") + " (" + i + ")");

			if (Constantes.INFLAR_ANTECIPADO) {
				objeto.inflar();
			} else {
				objeto.inflarParcial2();
			}

			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("Excluir", ex, formulario);
		}
	}

	private void atualizar(Objeto objeto) {
		if (!Util.confirmaAtualizacao(this)) {
			return;
		}

		try {
			int i = ArvoreUtil.atualizarObjetos(objeto);
			Util.mensagem(formulario, Mensagens.getString("label.sucesso") + " (" + i + ")");

			if (Constantes.INFLAR_ANTECIPADO) {
				objeto.inflar();
			} else {
				objeto.inflarParcial2();
			}

			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("Excluir", ex, formulario);
		}
	}

	@Override
	public void exibirPopup(Arvore arvore, Objeto selecionado, MouseEvent e) {
	}

	@Override
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

		textAreaArvore.setText(Util.normalizar(objeto.getInstrucaoArvore()));
		textAreaTabela.setText(Util.normalizar(objeto.getInstrucaoTabela()));
		textAreaUpdate.setText(Util.normalizar(objeto.getInstrucaoUpdate()));
		textAreaDelete.setText(Util.normalizar(objeto.getInstrucaoDelete()));
		textAreaInsert.setText(Util.normalizar(objeto.getInstrucaoInsert()));
		textAreaObserv.setText(Util.normalizar(objeto.getObservacao()));
		textAreaDescri.setText(Util.normalizar(objeto.getDescricao()));

		if (!Util.estaVazio(textAreaArvore.getText())) {
			fichario.addTab("label.arvore", Icones.ARVORE, textAreaArvore);
		}

		if (!Util.estaVazio(textAreaTabela.getText())) {
			fichario.addTab("label.tabela", Icones.TABELA, textAreaTabela);
		}

		if (!Util.estaVazio(textAreaUpdate.getText())) {
			fichario.addTab("label.update", Icones.UPDATE, textAreaUpdate);
		}

		if (!Util.estaVazio(textAreaDelete.getText())) {
			fichario.addTab("label.delete", Icones.EXCLUIR, textAreaDelete);
		}

		if (!Util.estaVazio(textAreaInsert.getText())) {
			fichario.addTab("label.insert", Icones.CRIAR, textAreaInsert);
		}

		if (!Util.estaVazio(textAreaObserv.getText())) {
			fichario.addTab("label.observacao", Icones.OBS, textAreaObserv);
		}

		if (!Util.estaVazio(textAreaDescri.getText())) {
			fichario.addTab("label.descricao", Icones.INFO, textAreaDescri);
		}

		controleButton();
	}

	private String getTituloSelecionado() {
		int i = fichario.getSelectedIndex();

		if (i == -1) {
			return "";
		}

		return fichario.getTitleAt(i);
	}

	public boolean ehTitulo(String chave, String s) {
		return Mensagens.getString(chave).equals(s);
	}

	public Arvore getArvore() {
		return arvore;
	}
}