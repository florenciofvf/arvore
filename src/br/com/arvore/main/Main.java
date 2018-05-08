package br.com.arvore.main;

import java.io.File;

import javax.swing.UIManager;

import br.com.arvore.banco.Conexao;
import br.com.arvore.util.Util;
import br.com.arvore.view.Formulario;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
			Conexao.getConnection();
		} catch (Exception ex) {
			String msg = Util.getStackTrace("Main", ex);
			Util.mensagem(null, msg);
			throw new Exception();
		}

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		File file = new File("modelo.xml");

		if (file.exists() && file.canRead()) {
			try {
				new Formulario(file);
			} catch (Exception ex) {
				Conexao.close();
				String msg = Util.getStackTrace("Formulario()", ex);
				Util.mensagem(null, msg);
				throw new Exception();
			}

		} else if (!file.exists()) {
			Util.mensagem(null, "Arquivo inexistente!\r\n\r\n" + file.getAbsolutePath());

		} else if (!file.canRead()) {
			Util.mensagem(null, "O arquivo nao pode ser lido!\r\n\r\n" + file.getAbsolutePath());

		} else {
			Util.mensagem(null, "Erro!");
		}
	}
}