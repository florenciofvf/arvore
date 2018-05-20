package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JDialog;

import br.com.arvore.comp.Button;
import br.com.arvore.comp.PanelCenter;
import br.com.arvore.util.Util;

public abstract class Dialogo extends JDialog {
	private static final long serialVersionUID = 1L;
	private Button buttonCancelar = new Button("label.cancelar");
	private Button buttonOK = new Button("label.ok");

	public Dialogo(Window window, int largura, int altura, String titulo, boolean buttonProcessar) {
		super(window);
		setModal(true);

		PanelCenter botoes = new PanelCenter(buttonCancelar);

		if (buttonProcessar) {
			botoes.add(buttonOK);
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(largura, altura);
		setLayout(new BorderLayout());
		add(BorderLayout.SOUTH, botoes);
		setLocationRelativeTo(window);
		Util.setActionESC(this);
		setTitle(titulo);
		configurar();
	}

	private void configurar() {
		buttonCancelar.addActionListener(e -> dispose());
		buttonOK.addActionListener(e -> processar());
	}

	protected abstract void processar();
}