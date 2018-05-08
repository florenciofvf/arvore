package br.com.arvore.modelo;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import br.com.arvore.Objeto;

public class ModeloArvore implements TreeModel {
	private final EventListenerList listenerList = new EventListenerList();
	private final Objeto raiz;

	public ModeloArvore(Objeto raiz) {
		this.raiz = raiz;
	}

	@Override
	public Object getRoot() {
		return raiz;
	}

	@Override
	public Object getChild(Object parent, int index) {
		return ((Objeto) parent).getObjeto(index);
	}

	@Override
	public int getChildCount(Object parent) {
		return ((Objeto) parent).getTotal();
	}

	@Override
	public boolean isLeaf(Object parent) {
		return ((Objeto) parent).estaVazio();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return ((Objeto) parent).getIndice((Objeto) child);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(TreeModelListener.class, l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(TreeModelListener.class, l);
	}

	public void treeStructureChanged(TreeModelEvent event) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				((TreeModelListener) listeners[i + 1]).treeStructureChanged(event);
			}
		}
	}
}