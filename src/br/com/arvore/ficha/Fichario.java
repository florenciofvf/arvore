package br.com.arvore.ficha;

import java.io.File;

import javax.swing.JTabbedPane;

import br.com.arvore.Objeto;
import br.com.arvore.arvore.Arvore;
import br.com.arvore.form.Formulario;
import br.com.arvore.mod.ModeloArvore;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;
import br.com.arvore.xml.XML;

public class Fichario extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	private final ListenerFicharioTitulo listenerFicharioTitulo = new ListenerFicharioTitulo();
	private final Formulario formulario;
	private FicharioListener listener;
	private Objeto raiz;

	public Fichario(Formulario formulario) {
		this.formulario = formulario;
		configurar();
	}

	public void setListener(FicharioListener listener) {
		this.listener = listener;
	}

	private void configurar() {
		addChangeListener(e -> abaSelecionada());
	}

	private void abaSelecionada() {
		int i = getSelectedIndex();

		if (i != -1) {
			FicharioAba ficharioAba = (FicharioAba) getComponentAt(i);
			Arvore arvore = ficharioAba.getArvore();
			listener.abaSelecionada(arvore, arvore.getObjetoSelecionado());
		}
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
				formulario.setTitle(Mensagens.getString("label.arvore"));
				raiz = XML.processar(file);
				addAba("label.objetos", raiz, true);
				formulario.setTitle(Mensagens.getString("label.arvore") + " - " + file.getAbsolutePath());
			} catch (Exception ex) {
				Util.stackTraceAndMessage("ABRIR ARQUIVO", ex, this);
			}
		}
	}

	private void limpar() {
		while (getTabCount() > 0) {
			listenerFicharioTitulo.excluirAba(0);
		}
	}

	private void addAba(String chaveTitulo, Objeto objeto, boolean clonar) throws Exception {
		objeto = objeto.clonar();

		if (Constantes.INFLAR_ANTECIPADO) {
			objeto.inflar();
		}

		Arvore arvore = new Arvore(new ModeloArvore(objeto));
		arvore.adicionarOuvinte(formulario.getListenerArvore());
		FicharioAba ficharioAba = new FicharioAba(arvore);
		addTab(Mensagens.getString(chaveTitulo), ficharioAba);

		FicharioTitulo titulo = new FicharioTitulo(this, clonar, listenerFicharioTitulo);
		setTabComponentAt(getTabCount() - 1, titulo);
	}

	public void atualizarArvore(Objeto objeto) {
		int i = getSelectedIndex();

		if (i != -1) {
			FicharioAba ficharioAba = (FicharioAba) getComponentAt(i);
			ficharioAba.atualizarArvore(objeto);
		}
	}

	public void excluirArvore(Objeto objeto) {
		int i = getSelectedIndex();

		if (i != -1) {
			FicharioAba ficharioAba = (FicharioAba) getComponentAt(i);
			ficharioAba.excluirArvore(objeto);
			abaSelecionada();
		}
	}

	public void criarModeloRegistro(Objeto objeto) {
		int i = getSelectedIndex();

		if (i != -1) {
			FicharioAba ficharioAba = (FicharioAba) getComponentAt(i);
			ficharioAba.criarModeloRegistro(objeto);
		}
	}

	private class ListenerFicharioTitulo implements FicharioTituloListener {
		@Override
		public void excluirAba(int indice) {
			FicharioAba ficharioAba = (FicharioAba) getComponentAt(indice);
			listener.arvoreExcluida(ficharioAba.getArvore());
			remove(indice);
		}

		@Override
		public void clonarAba() {
			try {
				addAba("label.objetos", raiz, false);
			} catch (Exception ex) {
				Util.stackTraceAndMessage("CLONAR ABA", ex, Fichario.this);
			}
		}
	}
}