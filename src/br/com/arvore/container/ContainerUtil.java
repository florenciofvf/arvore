package br.com.arvore.container;

import br.com.arvore.Objeto;
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

		Objeto selecionado = container.getObjetoSelecionado();

		if (selecionado != null) {
			Integer[] indices = selecionado.getIndices();
			xml.atributo(Constantes.INDICES, getStringIndices(indices));
		}

		xml.fecharTag();
		xml.finalizarTag(Constantes.CONTAINER);
	}

	private static String getStringIndices(Integer[] indices) {
		StringBuilder sb = new StringBuilder();

		for (Integer i : indices) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(i.toString());
		}

		return sb.toString();
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

			String strIndices = obj.getValorAtributo(Constantes.INDICES);
			if (Util.estaVazio(strIndices)) {
				return;
			}

			String[] strings = strIndices.split(",");
			int[] indices = new int[strings.length];

			for (int i = 0; i < indices.length; i++) {
				indices[i] = Integer.parseInt(strings[i]);
			}

			container.inflar(indices);
		} else {
			throw new IllegalStateException();
		}
	}
}