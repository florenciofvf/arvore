package br.com.arvore.comp;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

import br.com.arvore.util.Constantes;

public class SplitPane extends JSplitPane {
	private static final long serialVersionUID = 1L;
	private SplitPaneListener listener;

	public SplitPane() {
		super();
		config();
	}

	public SplitPane(int newOrientation) {
		super(newOrientation);
		config();
	}

	@Override
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);

		if (!Constantes.DESENHAR_SPLIT_CONTAINER) {
			return;
		}

		Dimension size = getSize();

		g.setColor(getBackground());

		if (orientation == HORIZONTAL_SPLIT) {
			g.drawRect(getDividerLocation(), 0, dividerSize - 1, getHeight() - 1);
		} else {
			g.drawRect(0, getDividerLocation(), size.width - 1, dividerSize - 1);
		}
	}

	public SplitPaneListener getListener() {
		return listener;
	}

	public void setListener(SplitPaneListener listener) {
		this.listener = listener;
	}

	private void config() {
		setOneTouchExpandable(!Constantes.DESENHAR_SPLIT_CONTAINER);
		setBorder(BorderFactory.createEmptyBorder());
		setContinuousLayout(true);
		setDividerSize(5);

		addPropertyChangeListener(evt -> {
			if (listener == null) {
				return;
			}

			SplitPane splitPane = (SplitPane) evt.getSource();
			String propertyName = evt.getPropertyName();

			if (SplitPane.DIVIDER_LOCATION_PROPERTY.equals(propertyName)) {
				listener.localizacao(splitPane.getDividerLocation());
			}
		});
	}
}