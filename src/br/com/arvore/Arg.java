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
		if ("inteiro".equalsIgnoreCase(tipo) || "inteiro".equalsIgnoreCase(outro)) {
			return new ArgInt();
		}

		if ("objeto".equalsIgnoreCase(tipo) || "objeto".equalsIgnoreCase(outro)) {
			return new ArgObject();
		}

		if ("longo".equalsIgnoreCase(tipo) || "longo".equalsIgnoreCase(outro)) {
			return new ArgLong();
		}

		if ("texto".equalsIgnoreCase(tipo) || "texto".equalsIgnoreCase(outro)) {
			return new ArgString();
		}

		if ("data".equalsIgnoreCase(tipo) || "data".equalsIgnoreCase(outro)) {
			return new ArgDate();
		}

		return new ArgString();
	}
}