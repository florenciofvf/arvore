package br.com.arvore.view;

import java.io.File;

import javax.swing.JTabbedPane;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.modelo.ModeloArvore;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;
import br.com.arvore.xml.XML;

public class Fichario extends JTabbedPane implements FicharioTituloListener {
	private static final long serialVersionUID = 1L;
	private final Formulario formulario;
	private Objeto raiz;

	public Fichario(Formulario formulario) {
		this.formulario = formulario;
	}

	public void abrirArquivo(File file, boolean msgInexistente, boolean msgSemConteudo, boolean msgNaoLeitura) {
		if (file != null) {
			if (!file.exists()) {
				if (msgInexistente)
					Util.mensagem(formulario,
							Mensagens.getString("erro.arquivo.inexistente") + "\n\n\n" + file.getAbsolutePath());
				return;
			}

			if (file.length() == 0) {
				if (msgSemConteudo)
					Util.mensagem(formulario,
							Mensagens.getString("erro.arquivo.vazio") + "\n\n\n" + file.getAbsolutePath());
				return;
			}

			if (!file.canRead()) {
				if (msgNaoLeitura)
					Util.mensagem(null,
							Mensagens.getString("erro.arquivo.leitura") + "\n\n\n" + file.getAbsolutePath());
				return;
			}

			limpar();

			try {
				raiz = XML.processar(file);
				addAba("label.objetos", raiz, true);
			} catch (Exception ex) {
				Util.stackTraceAndMessage("clonarAba()", ex, this);
			}
		}
	}

	private void limpar() {
		while (getTabCount() > 0) {
			excluirAba(0);
		}
	}

	private void addAba(String chaveTitulo, Objeto objeto, boolean clonar) throws Exception {
		objeto = objeto.clonar();

		if (Constantes.INFLAR_ANTECIPADO) {
			objeto.inflar();
		}

		Arvore arvore = new Arvore(new ModeloArvore(objeto));
		PainelAba painelAba = new PainelAba(formulario, arvore);
		addTab(Mensagens.getString(chaveTitulo), painelAba);

		FicharioTitulo titulo = new FicharioTitulo(this, clonar, this);
		setTabComponentAt(getTabCount() - 1, titulo);
	}

	@Override
	public void excluirAba(int indice) {
		remove(indice);
	}

	@Override
	public void clonarAba() {
		try {
			addAba("label.objetos", raiz, false);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("clonarAba()", ex, this);
		}
	}
}