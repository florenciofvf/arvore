package br.com.arvore.rotulo;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

import br.com.arvore.comp.Label;

public class Rotulo extends Label {
	private static final long serialVersionUID = 1L;
	private final List<RotuloListener> ouvintes;

	public Rotulo(String text) {
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		addMouseListener(mouseListener);
		ouvintes = new ArrayList<>();
		setText(text);
	}

	public void adicionarOuvinte(RotuloListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.add(listener);
	}

	private MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			ouvintes.forEach(o -> o.selecionarTitulo(Rotulo.this));
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

			ouvintes.forEach(o -> o.exibirPopup(Rotulo.this, e.getX(), e.getY()));
		}
	};
}