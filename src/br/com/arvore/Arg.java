package br.com.arvore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.com.arvore.arg.ArgDate;
import br.com.arvore.arg.ArgInt;
import br.com.arvore.arg.ArgLong;
import br.com.arvore.arg.ArgObject;
import br.com.arvore.arg.ArgString;

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

		if ("data".equals(tipo)) {
			return new ArgDate();
		}

		if ("objeto".equals(tipo)) {
			return new ArgObject();
		}

		return new ArgString();
	}
}