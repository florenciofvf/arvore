package br.com.arvore.componente;

import javax.swing.JCheckBox;

import br.com.arvore.util.Mensagens;

public class CheckBox extends JCheckBox {
	private static final long serialVersionUID = 1L;

	public CheckBox(String chaveRotulo, String chaveEstado) {
		super(Mensagens.getString(chaveRotulo), Mensagens.getStringBoolean(chaveEstado));
	}
}