package br.com.arvore.arg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.com.arvore.Arg;

public class ArgLong extends Arg {
	private long valor;

	@Override
	public void set(PreparedStatement psmt, int indice) throws Exception {
		psmt.setLong(indice, valor);
	}

	@Override
	public void get(ResultSet rs, int indice) throws Exception {
		valor = rs.getLong(indice);
	}

	@Override
	public String toString() {
		return "" + valor;
	}
}