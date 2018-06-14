package br.com.arvore.controle;

import br.com.arvore.Objeto;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.container.Container;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Obj;
import br.com.arvore.util.XMLUtil;

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

		return container.getObjetoSelecionado();
	}

	public static void salvarLayout(XMLUtil xml) {
		if (xml == null) {
			return;
		}

		xml.abrirFinalizarTag(Constantes.CONTROLE);
	}

	public static void aplicarLayout(Obj obj, Controle controle) {
		if (obj == null || controle == null) {
			return;
		}

		if (obj.isRight()) {
			Obj filho = obj.getFilho(0);

			if (filho.isControle()) {
				controle.selecionadoObjeto(null);
			} else {
				throw new IllegalStateException();
			}
		} else {
			throw new IllegalStateException();
		}
	}
}