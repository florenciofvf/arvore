package br.com.arvore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Obj {
	private final Map<String, String> atributos;
	private final List<Obj> filhos;
	private final String tipo;
	private Obj pai;

	public Obj(String tipo) {
		Objects.requireNonNull(tipo);
		atributos = new HashMap<>();
		filhos = new ArrayList<>();
		this.tipo = tipo;
	}

	public void adicionarFilho(Obj obj) {
		if (obj.getPai() != null) {
			obj.getPai().excluirFilho(obj);
		}

		filhos.add(obj);
		obj.pai = this;
	}

	public Obj getFilho(int indice) {
		return filhos.get(indice);
	}

	public void excluirFilho(Obj obj) {
		if (obj.getPai() == this) {
			filhos.remove(obj);
			obj.pai = null;
		}
	}

	public void adicionarAtributo(String nome, String valor) {
		atributos.put(nome, valor);
	}

	public Obj getPai() {
		return pai;
	}

	public String getValorAtributo(String nome) {
		return atributos.get(nome);
	}

	public String getTipo() {
		return tipo;
	}

	@Override
	public String toString() {
		return tipo + ": filhos=" + filhos.size();
	}
}