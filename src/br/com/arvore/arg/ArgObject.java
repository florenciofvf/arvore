package br.com.arvore.arg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.com.arvore.Arg;

public class ArgObject extends Arg {
	private Object valor;

	@Override
	public void set(PreparedStatement psmt, int indice) throws Exception {
		psmt.setObject(indice, valor);
	}

	@Override
	public void get(ResultSet rs, int indice) throws Exception {
		valor = rs.getObject(indice);
	}

	@Override
	public String toString() {
		return "" + valor;
	}
}