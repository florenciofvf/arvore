package br.com.arvore.dialog;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.SwingUtilities;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.mod.ModeloArvore;
import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Util;

public class DialogoObjeto extends Dialogo {
	private static final long serialVersionUID = 1L;

	public DialogoObjeto(Window window, Objeto objeto) {
		super(window, 400, 400, objeto.getTitulo(), false);
		montarLayout(objeto);
		setVisible(true);
		SwingUtilities.invokeLater(() -> toFront());
	}

	private void montarLayout(Objeto objeto) {
		try {
			ArvoreUtil.inflar(objeto);
			Arvore arvore = new Arvore(new ModeloArvore(objeto));

			add(BorderLayout.CENTER, new ScrollPane(arvore));
		} catch (Exception ex) {
			Util.stackTraceAndMessage("DESTACAR OBJETO", ex, this);
		}
	}

	protected void processar() {
	}
}