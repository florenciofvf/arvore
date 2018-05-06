package br.com.arvore.view;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.modelo.ModeloArvore;
import br.com.arvore.util.Mensagens;
import br.com.arvore.xml.XML;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private final Arvore arvore;
	private final Objeto raiz;

	public Formulario(File file) throws Exception {
		setTitle(Mensagens.getString("label.arvore"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		raiz = XML.processar(file);
		raiz.inflar();
		raiz.limpar();
		arvore = new Arvore(new ModeloArvore(raiz), null);
		setSize(500, 500);
		montarLayout();
		setVisible(true);
	}

	private void montarLayout() {
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, new ScrollPane(arvore));
	}
}