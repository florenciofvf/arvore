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

	public static Arg criar(String tipo, String outro) {
		if ("inteiro".equals(tipo) || "inteiro".equals(outro)) {
			return new ArgInt();
		}

		if ("objeto".equals(tipo) || "objeto".equals(outro)) {
			return new ArgObject();
		}

		if ("longo".equals(tipo) || "longo".equals(outro)) {
			return new ArgLong();
		}

		if ("texto".equals(tipo) || "texto".equals(outro)) {
			return new ArgString();
		}

		if ("data".equals(tipo) || "data".equals(outro)) {
			return new ArgDate();
		}

		return new ArgString();
	}
}