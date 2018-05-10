package br.com.arvore.view;

import java.awt.BorderLayout;

import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.PanelBorder;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.comp.SplitPane;
import br.com.arvore.comp.Table;
import br.com.arvore.comp.TableListener;
import br.com.arvore.modelo.ModeloOrdenacao;
import br.com.arvore.util.Util;

public class PainelRegistro extends PanelBorder {
	private static final long serialVersionUID = 1L;
	private final SplitPane splitPane = new SplitPane();
	private final Table table = new Table();

	public PainelRegistro(Arvore arvore) {
		add(BorderLayout.CENTER, splitPane);
		splitPane.setLeftComponent(new ScrollPane(arvore));
		splitPane.setRightComponent(new ScrollPane(table));
		splitPane.setDividerLocation(400);
	}

	public void setModeloOrdenacao(ModeloOrdenacao modelo, TableListener tableListener) {
		table.setModel(modelo);
		table.setTableListener(tableListener);
		Util.ajustar(table, getGraphics());
	}
}