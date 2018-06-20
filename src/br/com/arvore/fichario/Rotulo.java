package br.com.arvore.fichario;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;

import br.com.arvore.comp.Label;
import br.com.arvore.fichario.Titulo.TituloPopup;

public class Rotulo extends Label {
	private static final long serialVersionUID = 1L;
	private final TituloPopup tituloPopup;
	private final Fichario fichario;
	private final Titulo titulo;

	public Rotulo(Fichario fichario, Titulo titulo, TituloPopup tituloPopup) {
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		addMouseListener(mouseListener);
		this.tituloPopup = tituloPopup;
		this.fichario = fichario;
		this.titulo = titulo;
	}

	private MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int indice = fichario.indexOfTabComponent(titulo);

			if (indice != -1) {
				fichario.setSelectedIndex(indice);
			}
		};

		@Override
		public void mousePressed(MouseEvent e) {
			processar(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			processar(e);
		}

		private void processar(MouseEvent e) {
			if (!e.isPopupTrigger()) {
				return;
			}

			if (tituloPopup != null) {
				controleItensPopup(fichario.indexOfTabComponent(titulo));
				tituloPopup.show(titulo, e.getX(), e.getY());
			}
		}
	};

	private void controleItensPopup(int indice) {
		// Container primeiro = (Container) fichario.getComponentAt(0);
		// Divisor divisor = fichario.getDivisor();
		//
		// int validos = 0;
		//
		// for (int i = 1; i < fichario.getTabCount(); i++) {
		// Container container = (Container) fichario.getComponentAt(i);
		// if (!container.isMaximizado()) {
		// validos++;
		// }
		// }
		//
		// tituloPopup.itemExcluir.setEnabled(divisor != null &&
		// divisor.getOuvinte() != null);
		// tituloPopup.itemRLD.setEnabled(!primeiro.isMaximizado() && validos >
		// 0);
		//
		// if (indice != -1) {
		// Container selecionado = (Container) fichario.getComponentAt(indice);
		// tituloPopup.itemRestaurar.setSelected(!selecionado.isMaximizado());
		// tituloPopup.itemMaximizar.setSelected(selecionado.isMaximizado());
		// }
	}

	@Override
	public String getText() {
		if (fichario == null) {
			return null;
		}

		int indice = fichario.indexOfTabComponent(titulo);

		if (indice != -1) {
			return fichario.getTitleAt(indice);
		}

		return null;
	}
}