package br.com.arvore.compnte;

import javax.swing.Icon;
import javax.swing.JMenu;

import br.com.arvore.util.Mensagens;

public class Menu extends JMenu {
	private static final long serialVersionUID = 1L;

	public Menu(String chaveRotulo) {
		super(Mensagens.getString(chaveRotulo));
	}

	public Menu(String chaveRotulo, Icon icon) {
		super(Mensagens.getString(chaveRotulo));
		setIcon(icon);
	}
}