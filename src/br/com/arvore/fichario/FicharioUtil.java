package br.com.arvore.fichario;

import br.com.arvore.container.Container;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Obj;
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

	public static void ajusteScroll(Fichario fichario) {
		if (fichario == null) {
			return;
		}

		for (int i = 0; i < fichario.getTabCount(); i++) {
			Container c = (Container) fichario.getComponentAt(i);
			c.ajusteScroll();
		}
	}

	private static void illegalStateException() {
		throw new IllegalStateException();
	}

	public static void aplicarLayout(Obj obj, Fichario fichario) {
		if (obj == null || fichario == null) {
			return;
		}

		if (obj.isLeft()) {
			Obj filho = obj.getFilho(0);

			if (filho.isFichario()) {
				String abas = filho.getValorAtributo(Constantes.ABAS);
				int total = Integer.parseInt(abas);

				for (int i = 1; i < total; i++) {
					fichario.tituloListener.clonarAba(null);
				}

				for (int i = 0; i < total; i++) {
					String titulo = filho.getFilho(i).getValorAtributo(Constantes.TITULO);
					Container container = (Container) fichario.getComponentAt(i);
					container.aplicarLayout(filho.getFilho(i));
					fichario.setTitleAt(i, titulo);
				}
			} else if (filho.isDivisor()) {
				String orientacao = filho.getValorAtributo(Constantes.ORIENTACAO);

				if (Constantes.VERTICAL.equals(orientacao)) {
					fichario.tituloListener.cloneAcima(null);
					// TODO divisor.aplicarLayout(filho);
				} else if (Constantes.HORIZONTAL.equals(orientacao)) {
					fichario.tituloListener.cloneDireito(null);
					// TODO divisor.aplicarLayout(filho);
				} else {
					illegalStateException();
				}
			} else {
				illegalStateException();
			}

		} else if (obj.isRight()) {
			Obj filho = obj.getFilho(0);

			if (filho.isFichario()) {
				String abas = filho.getValorAtributo(Constantes.ABAS);
				int total = Integer.parseInt(abas);

				for (int i = 1; i < total; i++) {
					fichario.tituloListener.clonarAba(null);
				}

				for (int i = 0; i < total; i++) {
					String titulo = filho.getFilho(i).getValorAtributo(Constantes.TITULO);
					Container container = (Container) fichario.getComponentAt(i);
					container.aplicarLayout(filho.getFilho(i));
					fichario.setTitleAt(i, titulo);
				}
			} else if (filho.isDivisor()) {
				String orientacao = filho.getValorAtributo(Constantes.ORIENTACAO);

				if (Constantes.VERTICAL.equals(orientacao)) {
					fichario.tituloListener.cloneAcima(null);
					// TODO divisor.aplicarLayout(filho);
				} else if (Constantes.HORIZONTAL.equals(orientacao)) {
					fichario.tituloListener.cloneDireito(null);
					// TODO divisor.aplicarLayout(filho);
				} else {
					illegalStateException();
				}
			} else {
				illegalStateException();
			}

		} else {
			illegalStateException();
		}
	}
}