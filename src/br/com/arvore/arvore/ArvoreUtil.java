package br.com.arvore.arvore;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;

import br.com.arvore.Objeto;
import br.com.arvore.modelo.ModeloArvore;

public class ArvoreUtil {
	private ArvoreUtil() {
	}

	private static TreePath getTreePath(Objeto objeto) {
		List<Objeto> caminho = new ArrayList<>();

		Objeto o = objeto;

		while (o != null) {
			caminho.add(0, o);
			o = o.getPai();
		}

		return new TreePath(caminho.toArray(new Object[] {}));
	}

	public static void atualizarEstrutura(Arvore arvore, Objeto objeto) {
		ModeloArvore modelo = (ModeloArvore) arvore.getModel();

		TreePath path = getTreePath(objeto);
		TreeModelEvent event = new TreeModelEvent(objeto, path);

		modelo.treeStructureChanged(event);
	}

	public static void excluirEstrutura(Arvore arvore, Objeto objeto) {
		ModeloArvore modelo = (ModeloArvore) arvore.getModel();

		TreePath path = getTreePath(objeto);
		TreeModelEvent event = new TreeModelEvent(objeto, path);

		if (objeto.getPai() != null) {
			objeto.getPai().excluir(objeto);
		}

		modelo.treeNodesRemoved(event);
		arvore.setSelectionPath(null);
		SwingUtilities.updateComponentTreeUI(arvore);
	}
}