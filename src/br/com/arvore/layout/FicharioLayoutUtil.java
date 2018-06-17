package br.com.arvore.layout;

import br.com.arvore.container.Container;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.XMLUtil;

public class FicharioLayoutUtil {
	private FicharioLayoutUtil() {
	}

	public static void salvarLayout(XMLUtil xml, FicharioLayout fichario) {
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

	public static void ajusteScroll(FicharioLayout fichario) {
		if (fichario == null) {
			return;
		}

		for (int i = 0; i < fichario.getTabCount(); i++) {
			Container c = (Container) fichario.getComponentAt(i);
			c.ajusteScroll();
		}
	}
}