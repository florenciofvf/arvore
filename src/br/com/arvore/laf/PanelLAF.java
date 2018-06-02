package br.com.arvore.laf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

public class PanelLAF extends BasicPanelUI {
	private static final PanelLAF panel = new PanelLAF();

	public static ComponentUI createUI(JComponent c) {
		return panel;
	}

	@Override
	public void installUI(JComponent c) {
		JPanel panel = (JPanel) c;

		panel.setBorder(new EmptyBorder(panel.getInsets()));
		panel.setOpaque(false);
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		final int raio = 10;

		Color back = c.getBackground();
		Color color = new Color(back.getRed(), back.getGreen(), back.getBlue(), 128);
		g2.setColor(color);
		g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), raio, raio);

//		Color front = c.getForeground();
//		color = new Color(front.getRed(), front.getGreen(), front.getBlue(), 128);
//		g2.setColor(color);
//		g2.drawRoundRect(0, 0, c.getWidth(), c.getHeight(), raio, raio);
	}
}