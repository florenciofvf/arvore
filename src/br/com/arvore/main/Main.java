package br.com.arvore.main;

import javax.swing.UIManager;

import br.com.arvore.banco.Conexao;
import br.com.arvore.form.Formulario;
import br.com.arvore.util.Util;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
			Conexao.getConnection();
		} catch (Exception ex) {
			Util.stackTraceMessageAndException("Main", ex);
		}

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		Formulario formulario = new Formulario();
		formulario.setLocationRelativeTo(null);
		formulario.setVisible(true);
	}
}