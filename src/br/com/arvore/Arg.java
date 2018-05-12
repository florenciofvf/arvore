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
		final String inteiro = "inteiro";

		if (inteiro.equalsIgnoreCase(tipo) || inteiro.equalsIgnoreCase(outro)) {
			return new ArgInt();
		}

		final String objeto = "objeto";

		if (objeto.equalsIgnoreCase(tipo) || objeto.equalsIgnoreCase(outro)) {
			return new ArgObject();
		}

		final String longo = "longo";

		if (longo.equalsIgnoreCase(tipo) || longo.equalsIgnoreCase(outro)) {
			return new ArgLong();
		}

		final String texto = "texto";

		if (texto.equalsIgnoreCase(tipo) || texto.equalsIgnoreCase(outro)) {
			return new ArgString();
		}

		final String data = "data";

		if (data.equalsIgnoreCase(tipo) || data.equalsIgnoreCase(outro)) {
			return new ArgDate();
		}

		return new ArgString();
	}
}