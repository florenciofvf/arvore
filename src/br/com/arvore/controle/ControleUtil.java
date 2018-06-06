package br.com.arvore.controle;

import br.com.arvore.Objeto;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.container.Container;

public class ControleUtil {
	private ControleUtil() {
	}

	public static int getIndiceAbaAtiva(TabbedPane tabbedPane) {
		return tabbedPane.getSelectedIndex();
	}

	public static String getTituloAbaAtiva(TabbedPane tabbedPane) {
		int indice = getIndiceAbaAtiva(tabbedPane);

		if (indice == -1) {
			return null;
		}

		return tabbedPane.getTitleAt(indice);
	}

	public static Objeto getObjetoSelecionado(Container container) {
		if (container == null) {
			return null;
		}

		return container.getArvore().getObjetoSelecionado();
	}
}