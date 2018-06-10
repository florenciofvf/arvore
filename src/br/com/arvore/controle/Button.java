package br.com.arvore.controle;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class Button extends JButton {
	private static final long serialVersionUID = 1L;
	private final Color COR_NORMAL = new Color(160, 160, 160);
	private final Color COR_PRESS = new Color(64, 128, 255);
	private final Color COR_SOBRE = new Color(64, 64, 200);
	private final byte NORMAL = 0;
	private final byte PRESS = 1;
	private boolean sobre;
	private byte estado;
	private int desloc;

	public Button() {
		desloc = System.getProperty("os.name").toLowerCase().indexOf("indows") >= 0 ? 5 : 0;
		setBorder(new EmptyBorder(getInsets()));
		addMouseListener(mouseListener);
		setOpaque(false);
	}

	private MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			estado = PRESS;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			estado = NORMAL;
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			estado = NORMAL;
			sobre = false;
			repaint();
		};

		@Override
		public void mouseEntered(MouseEvent e) {
			sobre = true;
			repaint();
		};
	};

	@Override
	protected void paintComponent(Graphics g) {
		if (estado == NORMAL) {
			fillRect((Graphics2D) g, 0, 0, getWidth(), getHeight(), COR_NORMAL);
		} else if (estado == PRESS) {
			fillRect((Graphics2D) g, 0, 0, getWidth(), getHeight(), COR_PRESS);
		}
	}

	private void fillRect(Graphics2D g, int x, int y, int width, int height, Color color) {
		int inset = 1;

		int buttonHeight = height - (inset * 2);
		int buttonWidth = width - (inset * 2);
		int raio = buttonHeight;

		Composite composite = g.getComposite();
		Shape shape = g.getClip();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(inset, inset, buttonWidth + 1, buttonHeight + 1, raio, raio);

		Color inicio = color.darker();
		Color _final = color.brighter();
		Paint paint = new GradientPaint(0, inset, inicio, 0, buttonHeight, _final, false);
		g.setPaint(paint);

		g.fillRoundRect(inset, inset, buttonWidth, buttonHeight, raio, raio);

		int inset2 = 2;
		int buttonHeight2 = buttonHeight - (inset2 * 2);
		int buttonWidth2 = buttonWidth - (inset2 * 2);
		int raio2 = buttonHeight2;

		inicio = Color.WHITE;
		_final = color.brighter();
		paint = new GradientPaint(0, inset + inset2, inicio, 0, inset + inset2 + (buttonHeight2 / 2), color.brighter(),
				false);

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g.setPaint(paint);

		g.setClip(new RoundRectangle2D.Float(inset + inset2, inset + inset2, buttonWidth2, buttonHeight2 / 2,
				buttonHeight2 / 3, buttonHeight2 / 3));
		g.fillRoundRect(inset + inset2, inset + inset2, buttonWidth2, buttonHeight2, raio2, raio2);

		g.setComposite(composite);
		g.setClip(shape);

		String text = getText();
		Icon icon = getIcon();

		if (text == null || icon == null) {
			return;
		}

		icon.paintIcon(this, g, 10, 5);

		g.setColor(Color.BLACK.brighter());
		int l = getFontMetrics(getFont()).stringWidth(text);
		g.drawString(text, (width - l) / 2 + desloc, getFontMetrics(getFont()).getHeight() + 2);

		if (sobre) {
			g.setColor(COR_SOBRE);
			g.drawRoundRect(inset, inset, buttonWidth, buttonHeight, raio, raio);
		}
	}
}