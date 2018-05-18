package br.com.arvore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import br.com.arvore.arg.ArgDate;
import br.com.arvore.arg.ArgInt;
import br.com.arvore.arg.ArgLong;
import br.com.arvore.arg.ArgObject;
import br.com.arvore.arg.ArgString;

public abstract class Arg {

	public abstract void set(PreparedStatement psmt, int indice) throws Exception;

	public abstract void get(ResultSet rs, int indice) throws Exception;

	public abstract void set(StringBuilder sb);

	public abstract String getString();

	public static Arg criar(String tipo, String outro) {
		outro = outro == null ? "" : outro.toLowerCase();
		tipo = tipo == null ? "" : tipo.toLowerCase();

		Map<String, Arg> map = new HashMap<>();
		map.put("objeto", new ArgObject());
		map.put("texto", new ArgString());
		map.put("inteiro", new ArgInt());
		map.put("longo", new ArgLong());
		map.put("data", new ArgDate());

		Iterator<Map.Entry<String, Arg>> it = map.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, Arg> entry = it.next();
			String chave = entry.getKey();

			if (chave.equals(tipo) || chave.equals(outro)) {
				return entry.getValue();
			}
		}

		return new ArgString();
	}
}