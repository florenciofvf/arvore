package br.com.arvore.view;

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
import java.util.Objects;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import br.com.arvore.comp.Button;
import br.com.arvore.comp.Label;
import br.com.arvore.util.Mensagens;

public class FicharioTitulo extends JPanel {
	private static final long serialVersionUID = 1L;
	private final FicharioTituloListener ficharioTituloListener;
	private final JTabbedPane tabbedPane;
	private final boolean clonar;

	public FicharioTitulo(JTabbedPane tabbedPane, boolean clonar, FicharioTituloListener ficharioTituloListener) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		this.ficharioTituloListener = ficharioTituloListener;
		Objects.requireNonNull(ficharioTituloListener);
		Objects.requireNonNull(tabbedPane);
		this.tabbedPane = tabbedPane;
		this.clonar = clonar;
		setOpaque(false);
		add(new Title());
		add(new Ctrl());
	}

	private class Title extends Label {
		private static final long serialVersionUID = 1L;

		Title() {
			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		}

		@Override
		public String getText() {
			int i = tabbedPane.indexOfTabComponent(FicharioTitulo.this);

			if (i != -1) {
				return tabbedPane.getTitleAt(i);
			}

			return null;
		}
	}

	private class Ctrl extends Button implements ActionListener {
		private static final long serialVersionUID = 1L;

		public Ctrl() {
			int size = 17;
			setToolTipText(Mensagens.getString("label.fechar"));
			setBorder(BorderFactory.createEtchedBorder());
			setPreferredSize(new Dimension(size, size));
			addMouseListener(new MouseListener());
			setContentAreaFilled(false);
			setUI(new BasicButtonUI());
			setRolloverEnabled(true);
			setBorderPainted(false);
			addActionListener(this);
			setFocusable(false);
		}

		private class MouseListener extends MouseAdapter {
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
		}

		public void actionPerformed(ActionEvent e) {
			int i = tabbedPane.indexOfTabComponent(FicharioTitulo.this);

			if (i != -1 && ficharioTituloListener != null) {
				if (clonar) {
					ficharioTituloListener.clonarAba();
				} else {
					ficharioTituloListener.excluirAba(i);
				}
			}
		}

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
				g2.drawLine(delta - middle, deltaY, getWidth() - middle - 1, deltaY);
				g2.drawLine(deltaX, delta - 1, deltaX, getHeight() - middle - 1);
			}

			g2.dispose();
		}
	}
}