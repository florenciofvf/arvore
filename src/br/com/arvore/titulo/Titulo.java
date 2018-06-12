package br.com.arvore.titulo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;

import br.com.arvore.comp.Button;
import br.com.arvore.comp.Label;
import br.com.arvore.comp.Menu;
import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.Panel;
import br.com.arvore.comp.Popup;
import br.com.arvore.comp.RadioButtonMenuItem;
import br.com.arvore.container.Container;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class Titulo extends Panel {
	private static final long serialVersionUID = 1L;
	private final List<TituloListener> ouvintes;
	private final TituloPopup tituloPopup;
	private final Fichario fichario;
	private final boolean clonar;

	public Titulo(Fichario fichario, boolean clonar) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		tituloPopup = new TituloPopup(clonar);
		ouvintes = new ArrayList<>();
		this.fichario = fichario;
		this.clonar = clonar;
		add(new Rotulo());
		setOpaque(false);
		add(new Ctrl());
	}

	public void configMaximizarRestaurar(boolean maximizado) {
		tituloPopup.itemRestaurar.setSelected(!maximizado);
		tituloPopup.itemMaximizar.setSelected(maximizado);
	}

	public void configRLD(boolean replicarLD) {
		tituloPopup.itemRLD.setEnabled(replicarLD);
	}

	public void adicionarOuvinte(TituloListener listener) {
		ouvintes.add(listener);
	}

	private class Rotulo extends Label {
		private static final long serialVersionUID = 1L;

		Rotulo() {
			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			addMouseListener(mouseListener);
		}

		private MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int i = fichario.indexOfTabComponent(Titulo.this);

				if (i != -1) {
					fichario.setSelectedIndex(i);
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
					tituloPopup.itemExcluir
							.setEnabled(fichario.getDivisor() != null && fichario.getDivisor().getOuvinte() != null);
					tituloPopup.show(Titulo.this, e.getX(), e.getY());
				}
			}
		};

		@Override
		public String getText() {
			int i = fichario.indexOfTabComponent(Titulo.this);

			if (i != -1) {
				return fichario.getTitleAt(i);
			}

			return null;
		}
	}

	private void renomear() {
		int i = fichario.indexOfTabComponent(Titulo.this);

		if (i == -1) {
			return;
		}

		String titulo = fichario.getTitleAt(i);
		String novo = Util.getStringInput(Titulo.this, titulo);

		if (Util.estaVazio(novo)) {
			return;
		}

		fichario.setTitleAt(i, novo);
		SwingUtilities.updateComponentTreeUI(fichario);
	}

	private void maximizarRestaurar(boolean max) {
		int i = fichario.indexOfTabComponent(Titulo.this);

		if (i == -1) {
			return;
		}

		Container container = (Container) fichario.getComponentAt(i);

		if (max) {
			container.maximizar();
		} else {
			container.restaurar();
		}

		ouvintes.forEach(TituloListener::selecionarObjeto);
	}

	private class Ctrl extends Button {
		private static final long serialVersionUID = 1L;

		public Ctrl() {
			int size = 17;
			setToolTipText(Mensagens.getString("label.fechar"));
			setBorder(BorderFactory.createEtchedBorder());
			setPreferredSize(new Dimension(size, size));
			addActionListener(actionListener);
			addMouseListener(mouseListener);
			setContentAreaFilled(false);
			setUI(new BasicButtonUI());
			setRolloverEnabled(true);
			setBorderPainted(false);
			setFocusable(false);
		}

		private ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int indice = fichario.indexOfTabComponent(Titulo.this);

				if (indice != -1) {
					if (!clonar) {
						ouvintes.forEach(o -> o.excluirAba(indice));
					} else {
						ouvintes.forEach(TituloListener::clonarAba);
					}
				}
			}
		};

		private MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Component component = e.getComponent();

				if (component instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) component;
					button.setBorderPainted(true);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Component component = e.getComponent();

				if (component instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) component;
					button.setBorderPainted(false);
				}
			}
		};

		public void updateUI() {
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g.create();

			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}

			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);

			if (getModel().isRollover()) {
				g2.setColor(Color.MAGENTA);
			}

			int middle = 3;
			int deltaY = 9;
			int deltaX = 8;
			int delta = 6;

			if (!clonar) {
				g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
				g2.drawLine(delta, getHeight() - delta - 1, getWidth() - delta - 1, delta);
			} else {
				g2.drawLine(delta - middle + 2, deltaY - 1, getWidth() - middle - 3, deltaY - 1);
				g2.drawLine(deltaX, delta - 1, deltaX, getHeight() - middle - 3);
			}

			g2.dispose();
		}
	}

	private class TituloPopup extends Popup {
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

			itemRestaurar.addActionListener(
					e -> maximizarRestaurar(itemMaximizar.isSelected() && !itemRestaurar.isSelected()));
			itemMaximizar.addActionListener(
					e -> maximizarRestaurar(itemMaximizar.isSelected() && !itemRestaurar.isSelected()));
			itemExcluir.addActionListener(e -> ouvintes.forEach(TituloListener::excluirFichario));
			itemEsquerdo.addActionListener(e -> ouvintes.forEach(TituloListener::cloneEsquerdo));
			itemRLD.addActionListener(e -> ouvintes.forEach(TituloListener::clonarLocalDivisor));
			itemDireito.addActionListener(e -> ouvintes.forEach(TituloListener::cloneDireito));
			itemAbaixo.addActionListener(e -> ouvintes.forEach(TituloListener::cloneAbaixo));
			itemAcima.addActionListener(e -> ouvintes.forEach(TituloListener::cloneAcima));
			itemRenomear.addActionListener(e -> renomear());

			ButtonGroup grupo = new ButtonGroup();
			grupo.add(itemMaximizar);
			grupo.add(itemRestaurar);
		}
	}
}