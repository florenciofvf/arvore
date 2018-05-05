package br.com.arvore.util;

import java.util.List;

import br.com.arvore.Objeto;
import br.com.arvore.banco.Persistencia;

public class ArvoreUtil {
	private ArvoreUtil() {
	}

	public static List<Objeto> getObjetos(Objeto objeto) throws Exception {
		System.out.println(objeto.getConsulta());
		return Persistencia.getObjetos(objeto);
	}
}