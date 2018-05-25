package br.com.arvore.componente;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import br.com.arvore.util.Util;

public class TextArea extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private final JTextArea textArea;

	public TextArea() {
		textArea = new JTextArea();
		setViewportView(textArea);
	}

	public TextArea(String string) {
		textArea = new JTextArea(string);
		setViewportView(textArea);
	}

	public String getText() {
		return textArea.getText();
	}

	public void setText(String string) {
		textArea.setText(string);
	}

	public boolean estaVazio() {
		return Util.estaVazio(getText());
	}
}