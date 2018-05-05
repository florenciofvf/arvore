package br.com.arvore.comp;

import javax.swing.JSplitPane;

public class SplitPane extends JSplitPane {
	private static final long serialVersionUID = 1L;

	public SplitPane() {
		super();
		cfg();
	}

	public SplitPane(int newOrientation) {
		super(newOrientation);
		cfg();
	}

	private void cfg() {
		setOneTouchExpandable(true);
		setContinuousLayout(true);
	}
}