package br.com.arvore.laf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class ButtonLAF extends BasicButtonUI {
	private static final ButtonLAF button = new ButtonLAF();

	public static ComponentUI createUI(JComponent c) {
		return button;
	}

	@Override
	public Dimension getPreferredSize(JComponent c) {
		Dimension d = super.getPreferredSize(c);

		if (d.height < 20) {
			d.height = 20;
		}

		return d;
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);

		AbstractButton button = (AbstractButton) c;
		button.setBorder(new EmptyBorder(button.getInsets()));
		button.setOpaque(false);
	}

	@Override
	public void paintButtonPressed(Graphics g, AbstractButton b) {
		ButtonUtil.fillRect((Graphics2D) g, 0, 0, b.getWidth(), b.getHeight(), new Color(64, 128, 255));
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		AbstractButton b = (AbstractButton) c;
		ButtonUtil.fillRect((Graphics2D) g, 0, 0, b.getWidth(), b.getHeight(), new Color(160, 160, 160));
		super.paint(g, c);
	}
}