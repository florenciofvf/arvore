package br.com.arvore.titulo;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import br.com.arvore.comp.Menu;
import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.Panel;
import br.com.arvore.comp.Popup;
import br.com.arvore.comp.RadioButtonMenuItem;
import br.com.arvore.icone.Icone;
import br.com.arvore.icone.IconeListener;
import br.com.arvore.rotulo.Rotulo;
import br.com.arvore.rotulo.RotuloListener;
import br.com.arvore.util.Icones;

public class Titulo extends Panel {
	private static final long serialVersionUID = 1L;
	private final List<TituloListener> ouvintes;
	private final TituloPopup tituloPopup;
	private final Rotulo rotulo;
	private final Icone icone;

	public Titulo(String text, boolean principal) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		tituloPopup = new TituloPopup(principal);
		ouvintes = new ArrayList<>();
		icone = new Icone(principal);
		rotulo = new Rotulo(text);
		rotulo.adicionarOuvinte(rotuloListener);
		icone.adicionarOuvinte(iconeListener);
		setOpaque(false);
		add(rotulo);
		add(icone);
	}

	public void adicionarOuvinte(TituloListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.add(listener);
	}

	private void maximizarRestaurar() {
		boolean maximizar = tituloPopup.itemMaximizar.isSelected() && !tituloPopup.itemRestaurar.isSelected();

		if (maximizar) {
			ouvintes.forEach(o -> o.maximizar(Titulo.this));
		} else {
			ouvintes.forEach(o -> o.restaurar(Titulo.this));
		}
	}

	private RotuloListener rotuloListener = new RotuloListener() {
		@Override
		public void selecionarTitulo(Rotulo rotulo) {
			ouvintes.forEach(o -> o.selecionar(Titulo.this));
		}

		@Override
		public void exibirPopup(Rotulo rotulo, int x, int y) {
		}
	};

	private IconeListener iconeListener = new IconeListener() {
		@Override
		public void excluirAba(Icone icone) {
			ouvintes.forEach(o -> o.excluirAba(Titulo.this));
		}

		@Override
		public void clonarAba(Icone icone) {
			ouvintes.forEach(o -> o.clonarAba(Titulo.this));
		}
	};

	private class TituloPopup extends Popup {
		private static final long serialVersionUID = 1L;
		final RadioButtonMenuItem itemRestaurar = new RadioButtonMenuItem("label.restaurar", Icones.SPLIT, true);
		final RadioButtonMenuItem itemMaximizar = new RadioButtonMenuItem("label.maximizar", Icones.ARVORE);
		final MenuItem itemExcluir = new MenuItem("label.excluir_ficha", Icones.EXCLUIR2);
		final MenuItem itemReplicarLD = new MenuItem("label.replicar_local_divisor");
		final MenuItem itemEsquerdo = new MenuItem("label.a_esquerda");
		final MenuItem itemDireito = new MenuItem("label.a_direita");
		final MenuItem itemRenomear = new MenuItem("label.renomear");
		final Menu menuClonarEste = new Menu("label.clonar_este");
		final MenuItem itemAbaixo = new MenuItem("label.abaixo");
		final MenuItem itemAcima = new MenuItem("label.acima");

		public TituloPopup(boolean principal) {
			if (principal) {
				add(menuClonarEste);
				addSeparator();
				add(itemRenomear);
				addSeparator();
				add(itemExcluir);
				addSeparator();
				add(itemRestaurar);
				add(itemMaximizar);
				addSeparator();
				add(itemReplicarLD);
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
			itemReplicarLD.setEnabled(false);

			itemReplicarLD.addActionListener(e -> ouvintes.forEach(o -> o.clonarLocalDivisor(Titulo.this)));
			itemExcluir.addActionListener(e -> ouvintes.forEach(o -> o.excluirFichario(Titulo.this)));
			itemEsquerdo.addActionListener(e -> ouvintes.forEach(o -> o.cloneEsquerdo(Titulo.this)));
			itemDireito.addActionListener(e -> ouvintes.forEach(o -> o.cloneDireito(Titulo.this)));
			itemAbaixo.addActionListener(e -> ouvintes.forEach(o -> o.cloneAbaixo(Titulo.this)));
			itemRenomear.addActionListener(e -> ouvintes.forEach(o -> o.renomear(Titulo.this)));
			itemAcima.addActionListener(e -> ouvintes.forEach(o -> o.cloneAcima(Titulo.this)));
			itemRestaurar.addActionListener(e -> maximizarRestaurar());
			itemMaximizar.addActionListener(e -> maximizarRestaurar());

			ButtonGroup grupo = new ButtonGroup();
			grupo.add(itemMaximizar);
			grupo.add(itemRestaurar);
		}
	}
}