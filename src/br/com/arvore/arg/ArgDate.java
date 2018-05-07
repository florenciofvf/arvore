package br.com.arvore.arg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import br.com.arvore.Arg;

public class ArgDate extends Arg {
	private Date valor;

	@Override
	public void set(PreparedStatement psmt, int indice) throws Exception {
		if (valor != null) {
			psmt.setDate(indice, new java.sql.Date(valor.getTime()));
		}
	}

	@Override
	public void get(ResultSet rs, int indice) throws Exception {
		valor = rs.getDate(indice);
	}

	@Override
	public String toString() {
		return "" + valor;
	}
}