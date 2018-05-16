package br.com.arvore.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;

import br.com.arvore.Objeto;
import br.com.arvore.banco.Persistencia;
import br.com.arvore.comp.Arvore;
import br.com.arvore.modelo.ModeloArvore;

public class ArvoreUtil {
	private ArvoreUtil() {
	}

	public static List<Objeto> getObjetos(Objeto objeto) throws Exception {
		return Persistencia.getObjetos(objeto);
	}

	public static int excluirObjetos(Objeto objeto) throws Exception {
		return Persistencia.excluirObjetos(objeto);
	}

	public static int atualizarObjetos(Objeto objeto) throws Exception {
		return Persistencia.atualizarObjetos(objeto);
	}

	public static void atualizarEstrutura(Arvore arvore, Objeto objeto) {
		ModeloArvore modelo = (ModeloArvore) arvore.getModel();
		List<Objeto> caminho = new ArrayList<>();

		Objeto o = objeto;

		while (o != null) {
			caminho.add(0, o);
			o = o.getPai();
		}

		TreePath path = new TreePath(caminho.toArray(new Object[] {}));
		TreeModelEvent event = new TreeModelEvent(objeto, path);
		modelo.treeStructureChanged(event);
	}
}