package br.com.arvore.laf;

import javax.swing.UIDefaults;
import javax.swing.plaf.basic.BasicLookAndFeel;

public class ArvoreLAF extends BasicLookAndFeel {
	private static final long serialVersionUID = 1L;
	public static final String NOME = "Arvore";

	@Override
	public String getName() {
		return NOME;
	}

	@Override
	public String getID() {
		return NOME;
	}

	@Override
	public String getDescription() {
		return NOME;
	}

	@Override
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);
		table.put("ButtonUI", "br.com.arvore.laf.ButtonLAF");
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return false;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}
}