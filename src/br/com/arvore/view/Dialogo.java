package br.com.arvore.view;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import br.com.arvore.comp.Button;
import br.com.arvore.comp.PanelCenter;
import br.com.arvore.util.Util;

public abstract class Dialogo extends JDialog {
	private static final long serialVersionUID = 1L;
	private Button buttonCancelar = new Button("label.cancelar");
	private Button buttonOK = new Button("label.ok");

	public Dialogo(Formulario formulario, int largura, int altura, String titulo) {
		super(formulario, true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(largura, altura);
		setLayout(new BorderLayout());
		add(BorderLayout.SOUTH, new PanelCenter(buttonCancelar, buttonOK));
		setLocationRelativeTo(formulario);
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