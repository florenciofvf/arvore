package br.com.arvore.form;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import br.com.arvore.compnte.RadioButtonMenuItem;
import br.com.arvore.util.Util;

public class ItemAparencia extends RadioButtonMenuItem {
	private static final long serialVersionUID = 1L;
	private final String classe;

	public ItemAparencia(Formulario formulario, LookAndFeelInfo info) {
		this.setText(info.getName());
		classe = info.getClassName();

		addActionListener(e -> {
			try {
				UIManager.setLookAndFeel(classe);
				SwingUtilities.updateComponentTreeUI(formulario);
			} catch (Exception ex) {
				Util.stackTraceAndMessage(getClass().getName() + ".ItemMenu()", ex, formulario);
			}
		});
	}
}