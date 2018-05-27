package br.com.arvore.compnte;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import br.com.arvore.util.Mensagens;

public class TabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	@Override
	public void addTab(String chaveRotulo, Icon icon, Component component) {
		super.addTab(Mensagens.getString(chaveRotulo), icon, component);
	}

	@Override
	public void addTab(String chaveRotulo, Component component) {
		super.addTab(Mensagens.getString(chaveRotulo), component);
	}
}