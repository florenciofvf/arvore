package br.com.arvore.util;

import java.io.File;
import java.io.PrintWriter;

public class XMLUtil {
	private final String QL = "\r\n";
	private final PrintWriter pw;
	private int tab;

	public XMLUtil(File file) throws Exception {
		pw = new PrintWriter(file);
	}

	public XMLUtil prologo() {
		return print("<?xml").atributo("version", 1.0).atributo("encoding", "utf-8").print("?>").ql().ql();
	}

	public XMLUtil atributo(String nome, double valor) {
		return print(" " + nome + "=" + citar("" + valor));
	}

	public XMLUtil atributo(String nome, long valor) {
		return print(" " + nome + "=" + citar("" + valor));
	}

	public XMLUtil atributo(String nome, String valor) {
		return print(" " + nome + "=" + citar(valor));
	}

	public XMLUtil abrirTag(String nome) {
		tab++;
		return tabular().print("<" + nome);
	}

	public XMLUtil fecharTag() {
		return print(">").ql();
	}

	public XMLUtil abrirTag2(String nome) {
		return abrirTag(nome).fecharTag();
	}

	public XMLUtil finalizarTag(String nome) {
		tabular().print("</" + nome + ">").ql();
		tab--;
		return this;
	}

	public XMLUtil abrirFinalizarTag(String nome) {
		return abrirTag2(nome).finalizarTag(nome);
	}

	private XMLUtil tabular() {
		for (int i = 0; i < tab; i++) {
			print("\t");
		}

		return this;
	}

	private String citar(String valor) {
		return "\"" + valor + "\"";
	}

	private XMLUtil print(String s) {
		pw.print(s);
		return this;
	}

	private XMLUtil ql() {
		return print(QL);
	}

	public void close() {
		pw.close();
	}
}