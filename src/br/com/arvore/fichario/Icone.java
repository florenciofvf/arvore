package br.com.arvore.fichario;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.plaf.basic.BasicButtonUI;

import br.com.arvore.comp.Button;
import br.com.arvore.util.Mensagens;

public class Icone extends Button {
	private static final long serialVersionUID = 1L;
	private final List<IconeListener> ouvintes;
	private final Fichario fichario;
	private final boolean principal;
	private final Titulo titulo;

	public Icone(Fichario fichario, Titulo titulo, boolean principal) {
		setToolTipText(Mensagens.getString("label.fechar"));
		setBorder(BorderFactory.createEtchedBorder());
		setPreferredSize(new Dimension(17, 17));
		addActionListener(actionListener);
		addMouseListener(mouseListener);
		ouvintes = new ArrayList<>();
		setContentAreaFilled(false);
		setUI(new BasicButtonUI());
		this.principal = principal;
		this.fichario = fichario;
		setRolloverEnabled(true);
		setBorderPainted(false);
		this.titulo = titulo;
		setFocusable(false);
	}

	public void adicionarOuvinte(IconeListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.add(listener);
	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int indice = fichario.indexOfTabComponent(titulo);

			if (indice == -1) {
				return;
			}

			if (!principal) {
				ouvintes.forEach(o -> o.excluirAba(indice));
			} else {
				ouvintes.forEach(IconeListener::clonarAba);
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

		if (!principal) {
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2.drawLine(delta, getHeight() - delta - 1, getWidth() - delta - 1, delta);
		} else {
			g2.drawLine(delta - middle + 2, deltaY - 1, getWidth() - middle - 3, deltaY - 1);
			g2.drawLine(deltaX, delta - 1, deltaX, getHeight() - middle - 3);
		}

		g2.dispose();
	}
}