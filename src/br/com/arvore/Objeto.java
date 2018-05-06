package br.com.arvore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;

import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Util;

public class Objeto {
	private final List<Objeto> objetos;
	private final String titulo;
	private String nomeIcone;
	private String consulta;
	private Icon icone;
	private Objeto pai;
	private Arg[] args;

	public Objeto(String titulo) {
		Util.checarVazio(titulo, "erro.titulo_vazio");
		objetos = new ArrayList<>();
		this.titulo = titulo;
	}

	public Objeto clonar() {
		Objeto clone = new Objeto(titulo);
		clone.nomeIcone = nomeIcone;
		clone.consulta = consulta;
		clone.icone = icone;

		for (Objeto obj : objetos) {
			clone.add(obj.clonar());
		}

		return clone;
	}

	public void inflar() throws Exception {
		if (Util.estaVazio(consulta)) {
			for (Objeto obj : objetos) {
				obj.inflar();
			}
		} else {
			List<Objeto> listagem = ArvoreUtil.getObjetos(this);

			List<Objeto> tmp = new ArrayList<>(objetos);
			objetos.clear();

			for (Objeto obj : listagem) {
				add(obj);

				for (Objeto o : tmp) {
					obj.add(o.clonar());
				}

				obj.inflar();
			}
		}
	}

	public void limpar() {
		for (Objeto obj : objetos) {
			obj.limpar();
		}

		Iterator<Objeto> it = objetos.iterator();

		while (it.hasNext()) {
			Objeto obj = it.next();

			if (obj.estaVazio()) {
				it.remove();
			}
		}

		for (Objeto obj : objetos) {
			obj.limpar();
		}
	}

	public Objeto getPai() {
		return pai;
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

	public void excluir(Objeto obj) {
		if (obj.pai == this) {
			obj.pai = null;
			objetos.remove(obj);
		}
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

	public Arg[] getArgs() {
		return args;
	}

	public void setArgs(Arg[] args) {
		this.args = args;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Objeto) {
			return titulo.equals(((Objeto) obj).titulo);
		}

		return false;
	}
}