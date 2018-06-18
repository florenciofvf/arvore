package br.com.arvore.layout;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.Panel;
import br.com.arvore.comp.Popup;
import br.com.arvore.util.Icones;

public class TituloLayout extends Panel {
	private static final long serialVersionUID = 1L;
	private final List<TituloLayoutListener> ouvintes = new ArrayList<>();
	// private final FicharioLayout ficharioLayout;
	private final TituloPopup tituloPopup;

	public TituloLayout(FicharioLayout ficharioLayout, boolean clonar) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		this.tituloPopup = new TituloPopup();
		add(new RotuloLayout(ficharioLayout, this, tituloPopup));
		add(new IconeLayout(ficharioLayout, this, clonar, ouvintes));
		// this.ficharioLayout = ficharioLayout;
		setOpaque(false);
	}

	public void adicionarOuvinte(TituloLayoutListener listener) {
		ouvintes.add(listener);
	}

	protected class TituloPopup extends Popup {
		private static final long serialVersionUID = 1L;
		// final MenuItem itemAplicarModelo = new
		// MenuItem("label.aplicar_modelo", Icones.BOTTOM);
		final MenuItem itemSalvarModelo = new MenuItem("label.salvar_modelo", Icones.TOP);

		public TituloPopup() {
			add(itemSalvarModelo);
			// add(itemAplicarModelo);
		}
	}
}