package br.com.arvore.fichario;

import br.com.arvore.container.Container;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.XMLUtil;

public class FicharioUtil {
	private FicharioUtil() {
	}

	public static void salvarLayout(XMLUtil xml, Fichario fichario) {
		if (xml == null || fichario == null) {
			return;
		}

		xml.abrirTag(Constantes.FICHARIO);
		xml.atributo(Constantes.ABAS, fichario.getTabCount());
		xml.fecharTag();

		for (int i = 0; i < fichario.getTabCount(); i++) {
			Container container = (Container) fichario.getComponentAt(i);
			container.setTitulo(fichario.getTitleAt(i));
			container.salvarLayout(xml);
		}

		xml.finalizarTag(Constantes.FICHARIO);
	}
}