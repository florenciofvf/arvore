package br.com.arvore.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class ButtonUtil {
	private ButtonUtil() {
	}

	public static void fillRect(Graphics2D g, int x, int y, int width, int height, Color color) {
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
	}
}