package br.com.arvore.main;

import javax.swing.UIManager;

import br.com.arvore.banco.Conexao;
import br.com.arvore.formulario.Formulario;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Util;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
			if (!Constantes.INFLAR_DESATIVADO) {
				Conexao.getConnection();
			}
		} catch (Exception ex) {
			Util.stackTraceMessageAndException("Main", ex);
		}

		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

		Formulario formulario = new Formulario();
		formulario.setLocationRelativeTo(null);
		formulario.setVisible(true);
	}
}