package br.com.arvore.util;

import java.awt.Component;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import br.com.arvore.comp.TextArea;

public class Util {
	private static final boolean LOG_CONSOLE = false;

	private Util() {
	}

	public static void checarVazio(String s, String chave) {
		if (estaVazio(s)) {
			throw new IllegalArgumentException(Mensagens.getString(chave));
		}
	}

	public static boolean estaVazio(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static void mensagem(Component componente, String string) {
		TextArea textArea = new TextArea(string);
		textArea.setPreferredSize(new Dimension(500, 300));
		JOptionPane.showMessageDialog(componente, textArea, Mensagens.getString("label.atencao"),
				JOptionPane.PLAIN_MESSAGE);
	}

	public static String getStackTrace(String info, Exception ex) {
		StringWriter sw = new StringWriter();
		sw.append(info + "\r\n\r\n");

		if (ex != null) {
			if (LOG_CONSOLE) {
				ex.printStackTrace();
			} else {
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
			}
		}

		return sw.toString();
	}
}