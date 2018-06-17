package br.com.arvore.layout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.plaf.basic.BasicButtonUI;

import br.com.arvore.comp.Button;
import br.com.arvore.util.Mensagens;

public class IconeLayout extends Button {
	private static final long serialVersionUID = 1L;
	private final List<TituloLayoutListener> ouvintes;
	private final FicharioLayout ficharioLayout;
	private final TituloLayout tituloLayout;
	private final boolean clonar;

	public IconeLayout(FicharioLayout ficharioLayout, TituloLayout tituloLayout, boolean clonar, List<TituloLayoutListener> ouvintes) {
		setToolTipText(Mensagens.getString("label.fechar"));
		setBorder(BorderFactory.createEtchedBorder());
		setPreferredSize(new Dimension(17, 17));
		this.ficharioLayout = ficharioLayout;
		addActionListener(actionListener);
		this.tituloLayout = tituloLayout;
		addMouseListener(mouseListener);
		setContentAreaFilled(false);
		setUI(new BasicButtonUI());
		this.ouvintes = ouvintes;
		setRolloverEnabled(true);
		setBorderPainted(false);
		this.clonar = clonar;
		setFocusable(false);
	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int indice = ficharioLayout.indexOfTabComponent(tituloLayout);

			if (indice == -1) {
				return;
			}

			if (!clonar) {
				ouvintes.forEach(o -> o.excluirAba(indice));
			} else {
				ouvintes.forEach(TituloLayoutListener::clonarAba);
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