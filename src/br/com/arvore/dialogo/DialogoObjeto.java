package br.com.arvore.dialogo;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.SwingUtilities;

import br.com.arvore.ObjetoUtil;
import br.com.arvore.Objeto;
import br.com.arvore.arvore.Arvore;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.modelo.ModeloArvore;
import br.com.arvore.util.Util;

public class DialogoObjeto extends Dialogo {
	private static final long serialVersionUID = 1L;

	public DialogoObjeto(Frame frame, Objeto objeto) {
		super(frame, 400, 400, objeto.getTitulo(), false);
		montarLayout(objeto);
		setVisible(true);
		SwingUtilities.invokeLater(() -> toFront());
	}

	private void montarLayout(Objeto objeto) {
		try {
			ObjetoUtil.inflar(objeto);
			Arvore arvore = new Arvore(new ModeloArvore(objeto));
			arvore.desabilitarPopup();
			add(BorderLayout.CENTER, new ScrollPane(arvore));
		} catch (Exception ex) {
			Util.stackTraceAndMessage("DESTACAR OBJETO", ex, this);
		}
	}

	protected void processar() {
	}
}