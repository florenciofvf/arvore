package br.com.arvore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ArgString extends Arg {
	private String valor;

	@Override
	public void set(PreparedStatement psmt, int indice) throws Exception {
		psmt.setString(indice, valor);
	}

	@Override
	public void get(ResultSet rs, int indice) throws Exception {
		valor = rs.getString(indice);
	}

	@Override
	public String toString() {
		return valor;
	}
}