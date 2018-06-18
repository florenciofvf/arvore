package br.com.arvore.layout;

import java.awt.BorderLayout;

import br.com.arvore.comp.PanelBorder;
import br.com.arvore.fichario.Fichario;

public class ContainerLayout extends PanelBorder {
	private static final long serialVersionUID = 1L;

	public ContainerLayout() {
	}

	public void adicionar(Fichario fichario) {
		add(BorderLayout.CENTER, fichario);
	}
}