package br.com.arvore.main;

import java.io.File;

import javax.swing.UIManager;

import br.com.arvore.banco.Conexao;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;
import br.com.arvore.view.Formulario;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
			Conexao.getConnection();
		} catch (Exception ex) {
			Util.stackTraceMessageAndException("Main", ex);
		}

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		File file = new File("modelo.xml");

		if (file.exists() && file.canRead()) {

			try {
				Formulario formulario = new Formulario(file);
				formulario.setLocationRelativeTo(null);
				formulario.setVisible(true);
			} catch (Exception ex) {
				Conexao.close();
				Util.stackTraceMessageAndException("Formulario()", ex);
			}

		} else if (!file.exists()) {
			Util.mensagem(null, Mensagens.getString("erro.arquivo.inexistente") + "\n\n\n" + file.getAbsolutePath());

		} else if (!file.canRead()) {
			Util.mensagem(null, Mensagens.getString("erro.arquivo.leitura") + "\n\n\n" + file.getAbsolutePath());

		} else {
			Util.mensagem(null, "Erro!");
		}
	}
}