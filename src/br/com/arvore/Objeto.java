package br.com.arvore;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import br.com.arvore.util.Icones;
import br.com.arvore.util.Util;

public class Objeto {
	private final List<Objeto> objetos;
	private final String titulo;
	private String nomeIcone;
	private String consulta;
	private Long parametro;
	private Objeto pai;
	private Icon icone;

	public Objeto(String titulo) {
		Util.checarVazio(titulo, "erro.titulo_vazio");
		objetos = new ArrayList<>();
		this.titulo = titulo;
	}

	public Objeto clonar() {
		Objeto clone = new Objeto(titulo);
		clone.consulta = consulta;

		for (Objeto obj : objetos) {
			clone.add(obj.clonar());
		}

		return clone;
	}

	public Objeto clone() {
		Objeto clone = new Objeto(titulo);
		clone.consulta = consulta;

		return clone;
	}

	public Objeto getPai() {
		return pai;
	}

	public void inflar() throws Exception {
	}

	public Icon getIcone() {
		return icone;
	}

	public void setIcone(Icon icone) {
		this.icone = icone;
	}

	public void setNomeIcone(String nomeIcone) {
		if (Util.estaVazio(nomeIcone)) {
			nomeIcone = "um_pixel";
		}

		this.nomeIcone = nomeIcone;
		this.icone = Icones.getIcon(nomeIcone);
	}

	public String getNomeIcone() {
		return nomeIcone;
	}

	public List<Objeto> getObjetos() {
		return objetos;
	}

	public void add(Objeto objeto) {
		objeto.pai = this;
		objetos.add(objeto);
	}

	public boolean estaVazio() {
		return objetos.isEmpty();
	}

	public int getTotal() {
		return objetos.size();
	}

	public String getTitulo() {
		return titulo;
	}

	@Override
	public String toString() {
		return titulo;
	}

	public Long getParametro() {
		return parametro;
	}

	public void setParametro(Long parametro) {
		this.parametro = parametro;
	}

	public int getIndice(Objeto objeto) {
		return objetos.indexOf(objeto);
	}

	public Objeto getObjeto(int indice) {
		return objetos.get(indice);
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	@Override
	public int hashCode() {
		return titulo.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Objeto) {
			return titulo.equals(((Objeto) obj).titulo);
		}

		return false;
	}
}