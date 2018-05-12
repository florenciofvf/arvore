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
	private Objeto raiz;

	public Fichario(File file) throws Exception {
		raiz = XML.processar(file);
		addAba("label.objetos", raiz, true);
	}

	private void addAba(String chaveTitulo, Objeto objeto, boolean clonar) throws Exception {
		objeto = objeto.clonar();

		if (Constantes.INFLAR_ANTECIPADO) {
			objeto.inflar();
		}

		Arvore arvore = new Arvore(new ModeloArvore(objeto));
		PainelAba painelAba = new PainelAba(arvore);
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
			String msg = Util.getStackTrace("clonarAba()", ex);
			Util.mensagem(this, msg);
		}
	}
}