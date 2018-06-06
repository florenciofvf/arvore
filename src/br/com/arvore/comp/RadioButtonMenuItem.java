package br.com.arvore.comp;

import javax.swing.JRadioButtonMenuItem;

import br.com.arvore.util.Mensagens;

public class RadioButtonMenuItem extends JRadioButtonMenuItem {
	private static final long serialVersionUID = 1L;

	public RadioButtonMenuItem() {
	}

	public RadioButtonMenuItem(String chaveRotulo, boolean selecionado) {
		super(Mensagens.getString(chaveRotulo), selecionado);
	}

	public RadioButtonMenuItem(String chaveRotulo) {
		super(Mensagens.getString(chaveRotulo));
	}
}