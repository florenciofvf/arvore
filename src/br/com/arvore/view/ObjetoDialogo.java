package br.com.arvore.view;

import java.awt.BorderLayout;

import javax.swing.SwingUtilities;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.comp.TextArea;
import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class ObjetoDialogo extends Dialogo {
	private static final long serialVersionUID = 1L;
	private TextArea textAreaArvore = new TextArea();
	private TextArea textAreaTabela = new TextArea();
	private TextArea textAreaUpdate = new TextArea();
	private TextArea textAreaDelete = new TextArea();
	private TabbedPane fichario = new TabbedPane();
	private final PainelAba painelAba;
	private final byte ARVORE = 0;
	private final byte TABELA = 1;
	private final byte UPDATE = 2;
	private final byte DELETE = 3;
	private final Arvore arvore;
	private final Objeto objeto;

	public ObjetoDialogo(Formulario formulario, PainelAba painelAba, Arvore arvore, Objeto objeto) {
		super(formulario, 700, 700, objeto.getTitulo());
		this.painelAba = painelAba;
		this.arvore = arvore;
		this.objeto = objeto;
		montarLayout();
		setVisible(true);
		SwingUtilities.invokeLater(() -> toFront());
	}

	private void montarLayout() {
		textAreaArvore.setText(Util.normalizar(objeto.getInstrucaoArvore()));
		textAreaTabela.setText(Util.normalizar(objeto.getInstrucaoTabela()));
		textAreaUpdate.setText(Util.normalizar(objeto.getInstrucaoUpdate()));
		textAreaDelete.setText(Util.normalizar(objeto.getInstrucaoDelete()));

		fichario.addTab("label.arvore", textAreaArvore);
		fichario.addTab("label.tabela", textAreaTabela);
		fichario.addTab("label.update", textAreaUpdate);
		fichario.addTab("label.delete", textAreaDelete);

		add(BorderLayout.CENTER, fichario);
	}

	protected void processar() {
		if (ARVORE == fichario.getSelectedIndex()) {

			if (!textAreaArvore.estaVazio()) {
				objeto.setInstrucaoArvore(textAreaArvore.getText());
			}

			painelAba.atualizarArvore(objeto);
			dispose();

		} else if (TABELA == fichario.getSelectedIndex()) {

			if (textAreaTabela.estaVazio()) {
				Util.mensagem(this, Mensagens.getString("erro.sem_instrucao_tabela"));
				return;
			}

			objeto.setInstrucaoTabela(textAreaTabela.getText());
			painelAba.criarModeloRegistro(objeto);
			dispose();

		} else if (UPDATE == fichario.getSelectedIndex()) {

			if (textAreaUpdate.estaVazio()) {
				Util.mensagem(this, Mensagens.getString("erro.sem_instrucao_update"));
				return;
			}

			objeto.setInstrucaoUpdate(textAreaUpdate.getText());
			atualizarRegistros(objeto);

		} else if (DELETE == fichario.getSelectedIndex()) {

			if (textAreaDelete.estaVazio()) {
				Util.mensagem(this, Mensagens.getString("erro.sem_instrucao_delete"));
				return;
			}

			objeto.setInstrucaoDelete(textAreaDelete.getText());
			excluir(objeto);

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
			dispose();
		} catch (Exception ex) {
			Util.stackTraceAndMessage("Excluir", ex, this);
		}
	}

	private void atualizarRegistros(Objeto objeto) {
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
			dispose();
		} catch (Exception ex) {
			Util.stackTraceAndMessage("Excluir", ex, this);
		}
	}
}