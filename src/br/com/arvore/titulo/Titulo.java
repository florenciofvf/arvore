package br.com.arvore.titulo;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.SwingUtilities;

import br.com.arvore.comp.Menu;
import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.Panel;
import br.com.arvore.comp.Popup;
import br.com.arvore.comp.RadioButtonMenuItem;
import br.com.arvore.container.Container;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.util.Util;

public class Titulo extends Panel {
	private static final long serialVersionUID = 1L;
	private final List<TituloListener> ouvintes = new ArrayList<>();
	private final TituloPopup tituloPopup;
	private final Fichario fichario;

	public Titulo(Fichario fichario, boolean clonar) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		this.tituloPopup = new TituloPopup(clonar);
		add(new Rotulo(fichario, this, tituloPopup));
		add(new Icone(fichario, this, clonar, ouvintes));
		this.fichario = fichario;
		setOpaque(false);
	}

	public void adicionarOuvinte(TituloListener listener) {
		ouvintes.add(listener);
	}

	private void renomear() {
		int indice = fichario.indexOfTabComponent(this);

		if (indice == -1) {
			return;
		}

		String novo = Util.getStringInput(this, fichario.getTitleAt(indice));

		if (Util.estaVazio(novo)) {
			return;
		}

		fichario.setTitleAt(indice, novo);
		SwingUtilities.updateComponentTreeUI(fichario);
	}

	private void maximizarRestaurar() {
		int indice = fichario.indexOfTabComponent(this);

		if (indice == -1) {
			return;
		}

		boolean maximizar = tituloPopup.itemMaximizar.isSelected() && !tituloPopup.itemRestaurar.isSelected();
		Container container = (Container) fichario.getComponentAt(indice);

		if (maximizar) {
			container.maximizar();
		} else {
			container.restaurar();
		}

		ouvintes.forEach(TituloListener::selecionarObjeto);
	}

	protected class TituloPopup extends Popup {
		private static final long serialVersionUID = 1L;
		final RadioButtonMenuItem itemRestaurar = new RadioButtonMenuItem("label.restaurar", true);
		final RadioButtonMenuItem itemMaximizar = new RadioButtonMenuItem("label.maximizar");
		final MenuItem itemRLD = new MenuItem("label.replicar_local_divisor");
		final MenuItem itemExcluir = new MenuItem("label.excluir_ficha");
		final MenuItem itemEsquerdo = new MenuItem("label.a_esquerda");
		final MenuItem itemDireito = new MenuItem("label.a_direita");
		final MenuItem itemRenomear = new MenuItem("label.renomear");
		final Menu menuClonarEste = new Menu("label.clonar_este");
		final MenuItem itemAbaixo = new MenuItem("label.abaixo");
		final MenuItem itemAcima = new MenuItem("label.acima");

		public TituloPopup(boolean clonar) {
			if (clonar) {
				add(menuClonarEste);
				addSeparator();
				add(itemRenomear);
				addSeparator();
				add(itemExcluir);
				addSeparator();
				add(itemRestaurar);
				add(itemMaximizar);
				addSeparator();
				add(itemRLD);
			} else {
				add(itemRenomear);
				addSeparator();
				add(itemRestaurar);
				add(itemMaximizar);
			}

			menuClonarEste.add(itemEsquerdo);
			menuClonarEste.add(itemDireito);
			menuClonarEste.addSeparator();
			menuClonarEste.add(itemAcima);
			menuClonarEste.add(itemAbaixo);
			itemRLD.setEnabled(false);

			itemExcluir.addActionListener(e -> ouvintes.forEach(TituloListener::excluirFichario));
			itemEsquerdo.addActionListener(e -> ouvintes.forEach(TituloListener::cloneEsquerdo));
			itemRLD.addActionListener(e -> ouvintes.forEach(TituloListener::clonarLocalDivisor));
			itemDireito.addActionListener(e -> ouvintes.forEach(TituloListener::cloneDireito));
			itemAbaixo.addActionListener(e -> ouvintes.forEach(TituloListener::cloneAbaixo));
			itemAcima.addActionListener(e -> ouvintes.forEach(TituloListener::cloneAcima));
			itemRestaurar.addActionListener(e -> maximizarRestaurar());
			itemMaximizar.addActionListener(e -> maximizarRestaurar());
			itemRenomear.addActionListener(e -> renomear());

			ButtonGroup grupo = new ButtonGroup();
			grupo.add(itemMaximizar);
			grupo.add(itemRestaurar);
		}
	}
}