package br.com.arvore.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class Conexao {
	public static ResourceBundle bundle = ResourceBundle.getBundle("conexao");
	private static Connection conn;

	public static Connection getConnection() throws Exception {
		if (conn == null || conn.isClosed()) {
			Class.forName(getString("driver"));

			String url = getString("url");
			String usr = getString("login");
			String psw = getString("senha");

			conn = DriverManager.getConnection(url, usr, psw);
		}

		return conn;
	}

	public static void close() throws Exception {
		if (conn != null && !conn.isClosed()) {
			conn.close();
		}
	}

	public static String getString(String chave) {
		return bundle.getString(chave);
	}
}