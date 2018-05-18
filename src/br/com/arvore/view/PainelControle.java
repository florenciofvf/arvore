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
	private final byte ARVORE = 0;
	private final byte TABELA = 1;
	private final byte UPDATE = 2;
	private final byte DELETE = 3;
	private final byte INSERT = 4;
	private Arvore arvore;
	private Objeto objeto;

	public PainelControle(Formulario formulario) {
		this.formulario = formulario;
		montarLayout();
		configurar();
	}

	private void configurar() {
		buttonExecutar.addActionListener(e -> processar());

		fichario.addChangeListener(e -> {
			int i = fichario.getSelectedIndex();

			if (i != -1) {
				buttonExecutar.setIcon(fichario.getIconAt(i));
			}
		});
	}

	private void montarLayout() {
		add(BorderLayout.NORTH, labelStatus);
		add(BorderLayout.CENTER, fichario);
		add(BorderLayout.SOUTH, new PanelLeft(buttonExecutar));
	}

	private void processar() {
		if (ARVORE == fichario.getSelectedIndex()) {
			if (!textAreaArvore.estaVazio()) {
				objeto.setInstrucaoArvore(textAreaArvore.getText());
			}
			formulario.getFichario().atualizarArvore(objeto);

		} else if (TABELA == fichario.getSelectedIndex()) {
			if (textAreaTabela.estaVazio()) {
				Util.mensagem(this, Mensagens.getString("erro.sem_instrucao_tabela"));
				return;
			}
			objeto.setInstrucaoTabela(textAreaTabela.getText());
			formulario.getFichario().criarModeloRegistro(objeto);

		} else if (UPDATE == fichario.getSelectedIndex()) {
			if (textAreaUpdate.estaVazio()) {
				Util.mensagem(this, Mensagens.getString("erro.sem_instrucao_update"));
				return;
			}
			objeto.setInstrucaoUpdate(textAreaUpdate.getText());
			atualizar(objeto);

		} else if (DELETE == fichario.getSelectedIndex()) {
			if (textAreaDelete.estaVazio()) {
				Util.mensagem(this, Mensagens.getString("erro.sem_instrucao_delete"));
				return;
			}
			objeto.setInstrucaoDelete(textAreaDelete.getText());
			excluir(objeto);

		} else if (INSERT == fichario.getSelectedIndex()) {
			if (textAreaDelete.estaVazio()) {
				Util.mensagem(this, Mensagens.getString("erro.sem_instrucao_insert"));
				return;
			}
			objeto.setInstrucaoInsert(textAreaDelete.getText());
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
			Util.stackTraceAndMessage("Excluir", ex, this);
		}
	}

	private void excluir(Objeto objeto) {
		if (!Util.confirmaExclusao(this)) {
			return;
		}

		try {
			int i = ArvoreUtil.excluirObjetos(objeto);
			Util.mensagem(this, Mensagens.getString("label.sucesso") + " (" + i + ")");

			if (Constantes.INFLAR_ANTECIPADO) {
				objeto.inflar();
			} else {
				objeto.inflarParcial2();
			}

			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("Excluir", ex, this);
		}
	}

	private void atualizar(Objeto objeto) {
		if (!Util.confirmaAtualizacao(this)) {
			return;
		}

		try {
			int i = ArvoreUtil.atualizarObjetos(objeto);
			Util.mensagem(this, Mensagens.getString("label.sucesso") + " (" + i + ")");

			if (Constantes.INFLAR_ANTECIPADO) {
				objeto.inflar();
			} else {
				objeto.inflarParcial2();
			}

			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("Excluir", ex, this);
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

		int i = fichario.getSelectedIndex();
		buttonExecutar.setVisible(i != -1 && i <= INSERT);

		if (i != -1 && i <= INSERT) {
			buttonExecutar.setIcon(fichario.getIconAt(i));
		}
	}

	public Arvore getArvore() {
		return arvore;
	}
}