package br.com.arvore.main;

import java.io.File;

import javax.swing.UIManager;

import br.com.arvore.util.Util;
import br.com.arvore.view.Formulario;

public class Main {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		File file = new File("modelo.xml");

		if (file.exists() && file.canRead()) {
			new Formulario(file);

		} else if (!file.exists()) {
			Util.mensagem(null, "Arquivo inexistente!\r\n\r\n" + file.getAbsolutePath());

		} else if (!file.canRead()) {
			Util.mensagem(null, "O arquivo nao pode ser lido!\r\n\r\n" + file.getAbsolutePath());

		} else {
			Util.mensagem(null, "Erro!");
		}
	}
}