package br.com.arvore.compnte;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

public class SplitPane extends JSplitPane {
	private static final long serialVersionUID = 1L;
	private SplitPaneListener listener;

	public SplitPane() {
		super();
		cfg();
	}

	public SplitPane(int newOrientation) {
		super(newOrientation);
		cfg();
	}

	public SplitPaneListener getListener() {
		return listener;
	}

	public void setListener(SplitPaneListener listener) {
		this.listener = listener;
	}

	private void cfg() {
		setBorder(BorderFactory.createEmptyBorder());
		setOneTouchExpandable(true);
		setContinuousLayout(true);

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