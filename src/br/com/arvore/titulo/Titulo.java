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
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import br.com.arvore.componente.Button;
import br.com.arvore.componente.Label;
import br.com.arvore.componente.Panel;
import br.com.arvore.util.Mensagens;

public class Titulo extends Panel {
	private static final long serialVersionUID = 1L;
	private final List<TituloListener> ouvintes;
	private final JTabbedPane tabbedPane;
	private final boolean clonar;

	public Titulo(JTabbedPane tabbedPane, boolean clonar) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		this.tabbedPane = tabbedPane;
		ouvintes = new ArrayList<>();
		this.clonar = clonar;
		add(new Rotulo());
		setOpaque(false);
		add(new Ctrl());
	}

	public void adicionarOuvinte(TituloListener listener) {
		ouvintes.add(listener);
	}

	public void limparOuvintes() {
		ouvintes.clear();
	}

	private void notificarExcluirAba(int indice) {
		for (TituloListener ouvinte : ouvintes) {
			ouvinte.excluirAba(indice);
		}
	}

	private void notificarClonarAba() {
		for (TituloListener ouvinte : ouvintes) {
			ouvinte.clonarAba();
		}
	}

	private class Rotulo extends Label {
		private static final long serialVersionUID = 1L;

		Rotulo() {
			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		}

		@Override
		public String getText() {
			int i = tabbedPane.indexOfTabComponent(Titulo.this);

			if (i != -1) {
				return tabbedPane.getTitleAt(i);
			}

			return null;
		}
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
				int indice = tabbedPane.indexOfTabComponent(Titulo.this);

				if (indice != -1) {
					if (!clonar) {
						notificarExcluirAba(indice);
					} else {
						notificarClonarAba();
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
}