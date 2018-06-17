package br.com.arvore.layout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;

import br.com.arvore.comp.Label;
import br.com.arvore.layout.TituloLayout.TituloPopup;

public class RotuloLayout extends Label {
	private static final long serialVersionUID = 1L;
	private final FicharioLayout ficharioLayout;
	private final TituloLayout tituloLayout;
	private final TituloPopup tituloPopup;

	public RotuloLayout(FicharioLayout ficharioLayout, TituloLayout tituloLayout, TituloPopup tituloPopup) {
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		this.ficharioLayout = ficharioLayout;
		this.tituloLayout = tituloLayout;
		addMouseListener(mouseListener);
		this.tituloPopup = tituloPopup;
	}

	private MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int indice = ficharioLayout.indexOfTabComponent(tituloLayout);

			if (indice != -1) {
				ficharioLayout.setSelectedIndex(indice);
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
				tituloPopup.show(tituloLayout, e.getX(), e.getY());
			}
		}
	};

	@Override
	public String getText() {
		if (ficharioLayout == null) {
			return null;
		}

		int indice = ficharioLayout.indexOfTabComponent(tituloLayout);

		if (indice != -1) {
			return ficharioLayout.getTitleAt(indice);
		}

		return null;
	}
}