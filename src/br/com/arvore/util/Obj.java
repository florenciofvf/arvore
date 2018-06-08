package br.com.arvore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Obj {
	private Map<String, String> attr = new HashMap<>();
	private List<Obj> filhos = new ArrayList<>();
	private final String tipo;
	private Obj pai;

	public Obj(String tipo) {
		this.tipo = tipo;
	}

	public void add(Obj obj) {
		filhos.add(obj);
		obj.pai = this;
	}

	public Obj getPai() {
		return pai;
	}

	public String getAttr(String nome) {
		return attr.get(nome);
	}

	public String getTipo() {
		return tipo;
	}
}