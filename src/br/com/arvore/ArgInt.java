package br.com.arvore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ArgInt extends Arg {
	private int valor;

	@Override
	public void set(PreparedStatement psmt, int indice) throws Exception {
		psmt.setInt(indice, valor);
	}

	@Override
	public void get(ResultSet rs, int indice) throws Exception {
		valor = rs.getInt(indice);
	}

	@Override
	public String toString() {
		return "" + valor;
	}
}