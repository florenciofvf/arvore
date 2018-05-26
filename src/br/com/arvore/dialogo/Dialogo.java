package br.com.arvore.dialogo;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;

import br.com.arvore.componente.Button;
import br.com.arvore.componente.PanelCenter;
import br.com.arvore.util.Util;

public abstract class Dialogo extends JDialog {
	private static final long serialVersionUID = 1L;
	private Button buttonCancelar = new Button("label.cancelar");
	private Button buttonOK = new Button("label.ok");

	public Dialogo(Frame frame, int largura, int altura, String titulo, boolean buttonProcessar) {
		super(frame, true);

		PanelCenter botoes = new PanelCenter(buttonCancelar);

		if (buttonProcessar) {
			botoes.add(buttonOK);
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(largura, altura);
		setLayout(new BorderLayout());
		add(BorderLayout.SOUTH, botoes);
		setLocationRelativeTo(frame);
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