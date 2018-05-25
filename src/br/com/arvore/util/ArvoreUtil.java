package br.com.arvore.util;

import java.util.List;

import br.com.arvore.Objeto;
import br.com.arvore.banco.Persistencia;

public class ArvoreUtil {
	private ArvoreUtil() {
	}

	public static List<Objeto> getObjetos(Objeto objeto) throws Exception {
		return Persistencia.getObjetos(objeto);
	}

	public static int excluirObjetos(Objeto objeto) throws Exception {
		return Persistencia.excluirObjetos(objeto);
	}

	public static int inserirObjeto(Objeto objeto) throws Exception {
		return Persistencia.inserirObjeto(objeto);
	}

	public static int atualizarObjetos(Objeto objeto) throws Exception {
		return Persistencia.atualizarObjetos(objeto);
	}

	public static void inflar(Objeto objeto) throws Exception {
		if (Constantes.INFLAR_ANTECIPADO) {
			objeto.inflar();
		} else {
			objeto.inflarParcial2();
		}
	}

	public static void validarDependencia(Objeto objeto) {
		if (objeto.getPai() == null) {
			int[] parametros = ParametroUtil.getIndiceParametros(objeto.getInstrucaoArvore());

			if (parametros.length > 0) {
				throw new IllegalStateException(Mensagens.getString("erro.arquivo_dependente"));
			}
		}
	}
}