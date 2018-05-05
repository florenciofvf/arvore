package br.com.arvore.comp;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
}