package br.com.arvore.layout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;

import br.com.arvore.comp.Label;
import br.com.arvore.layout.TituloLayout.TituloPopup;

public class RotuloLayout extends Label {
	private static final long serialVersionUID = 1L;
	private final TituloPopup tituloPopup;
	private final FicharioLayout fichario;
	private final TituloLayout titulo;

	public RotuloLayout(FicharioLayout fichario, TituloLayout titulo, TituloPopup tituloPopup) {
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
				tituloPopup.show(titulo, e.getX(), e.getY());
			}
		}
	};

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