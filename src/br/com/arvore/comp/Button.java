package br.com.arvore.comp;

import javax.swing.Icon;
import javax.swing.JButton;

import br.com.arvore.util.Mensagens;

public class Button extends JButton {
	private static final long serialVersionUID = 1L;

	public Button() {
	}

	public Button(String chaveRotulo) {
		super(Mensagens.getString(chaveRotulo));
	}

	public Button(String chaveRotulo, Icon icon) {
		super(icon);
		setToolTipText(Mensagens.getString(chaveRotulo));
	}
}