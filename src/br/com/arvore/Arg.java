package br.com.arvore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class Arg {

	public abstract void set(PreparedStatement psmt, int indice) throws Exception;

	public abstract void get(ResultSet rs, int indice) throws Exception;

	public static Arg criar(String tipo) {
		if ("inteiro".equals(tipo)) {
			return new ArgInt();
		}

		if ("longo".equals(tipo)) {
			return new ArgLong();
		}

		if ("texto".equals(tipo)) {
			return new ArgString();
		}

		return new ArgString();
	}
}