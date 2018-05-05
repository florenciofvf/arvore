package br.com.arvore.comp;

import java.awt.Component;

import javax.swing.JTabbedPane;

import br.com.arvore.util.Mensagens;

public class TabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	@Override
	public void addTab(String chaveRotulo, Component component) {
		super.addTab(Mensagens.getString(chaveRotulo), component);
	}
}