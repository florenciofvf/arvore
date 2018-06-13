package br.com.arvore.container;

import br.com.arvore.util.Constantes;
import br.com.arvore.util.Obj;
import br.com.arvore.util.Util;
import br.com.arvore.util.XMLUtil;

public class ContainerUtil {
	private ContainerUtil() {
	}

	public static void salvarLayout(XMLUtil xml, Container container) {
		if (xml == null || container == null) {
			return;
		}

		xml.abrirTag(Constantes.CONTAINER);
		xml.atributo(Constantes.TITULO, Util.escapar(container.getTitulo()));
		xml.atributo(Constantes.LOCAL_DIV, container.getDividerLocation());
		xml.atributo(Constantes.MAXIMIZADO, container.isMaximizado());
		xml.fecharTag();
		xml.finalizarTag(Constantes.CONTAINER);
	}

	public static void aplicarLayout(Obj obj, Container container) {
		if (obj == null || container == null) {
			return;
		}

		if (obj.isContainer()) {
			String localDiv = obj.getValorAtributo(Constantes.LOCAL_DIV);
			String max = obj.getValorAtributo(Constantes.MAXIMIZADO);

			if (Boolean.parseBoolean(max)) {
				container.maximizar();
			}

			int local = Integer.parseInt(localDiv);
			container.setDividerLocation(local);
		} else {
			throw new IllegalStateException();
		}
	}
}