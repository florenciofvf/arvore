package br.com.arvore;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Util;

public class Objeto {
	private final List<Objeto> objetos;
	private final List<Objeto> nativos;
	private boolean nativoArmazenados;
	private boolean desabilitado;
	private boolean manterVazio;
	private final String titulo;
	private String nomeSubIcone;
	private String nomeIcone;
	private String consulta;
	private String pesquisa;
	private Icon subIcone;
	private Icon icone;
	private Arg[] args;
	private Objeto pai;

	public Objeto(String titulo) {
		Util.checarVazio(titulo, "erro.titulo_vazio");
		objetos = new ArrayList<>();
		nativos = new ArrayList<>();
		this.titulo = titulo;
	}

	public Objeto clonar() {
		Objeto clone = new Objeto(titulo);
		clone.desabilitado = desabilitado;
		clone.nomeSubIcone = nomeSubIcone;
		clone.manterVazio = manterVazio;
		clone.nomeIcone = nomeIcone;
		clone.consulta = consulta;
		clone.pesquisa = pesquisa;
		clone.subIcone = subIcone;
		clone.icone = icone;

		for (Objeto obj : objetos) {
			clone.add(obj.clonar());
		}

		return clone;
	}

	public void inflar() throws Exception {
		if (!nativoArmazenados) {
			for (Objeto obj : objetos) {
				nativos.add(obj.clonar());
			}

			nativoArmazenados = true;
		}

		limpar();

		if (Util.estaVazio(consulta)) {
			for (Objeto obj : nativos) {
				if (obj.isDesabilitado()) {
					continue;
				}

				Objeto nativo = obj.clonar();
				add(nativo);
				nativo.inflar();

				if (nativo.estaVazio() && !nativo.isManterVazio()) {
					excluir(nativo);
				}
			}
		} else {
			List<Objeto> listagem = ArvoreUtil.getObjetos(this);

			for (Objeto obj : listagem) {
				if (obj.isDesabilitado()) {
					continue;
				}

				obj.setIcone(getSubIcone());
				add(obj);

				for (Objeto o : nativos) {
					if (o.isDesabilitado()) {
						continue;
					}

					Objeto nativo = o.clonar();
					obj.add(nativo);
					nativo.inflar();

					if (nativo.estaVazio() && !nativo.isManterVazio()) {
						obj.excluir(nativo);
					}
				}
			}
		}
	}

	public void inflarParcial() throws Exception {
		if (!nativoArmazenados) {
			for (Objeto obj : objetos) {
				nativos.add(obj.clonar());
			}

			nativoArmazenados = true;
			inflarParcial2();
		}
	}

	public void inflarParcial2() throws Exception {
		if (!nativoArmazenados) {
			return;
		}

		limpar();

		if (Util.estaVazio(consulta)) {
			for (Objeto obj : nativos) {
				if (obj.isDesabilitado()) {
					continue;
				}

				Objeto nativo = obj.clonar();
				add(nativo);
				nativo.inflarParcial();

				if (nativo.estaVazio() && !nativo.isManterVazio()) {
					excluir(nativo);
				}
			}
		} else {
			List<Objeto> listagem = ArvoreUtil.getObjetos(this);

			for (Objeto obj : listagem) {
				if (obj.isDesabilitado()) {
					continue;
				}

				obj.setIcone(getSubIcone());
				add(obj);

				for (Objeto o : nativos) {
					if (o.isDesabilitado()) {
						continue;
					}

					Objeto nativo = o.clonar();
					obj.add(nativo);
				}
			}
		}
	}

	public Objeto getPai() {
		return pai;
	}

	public Icon getIcone() {
		return icone;
	}

	public Icon getSubIcone() {
		return subIcone;
	}

	public void setIcone(Icon icone) {
		this.icone = icone;
	}

	public void setSubIcone(Icon subIcone) {
		this.subIcone = subIcone;
	}

	public void setNomeIcone(String nomeIcone) {
		if (Util.estaVazio(nomeIcone)) {
			nomeIcone = "um_pixel";
		}

		this.nomeIcone = nomeIcone;
		this.icone = Icones.getIcon(nomeIcone);
	}

	public void setNomeSubIcone(String nomeSubIcone) {
		if (Util.estaVazio(nomeSubIcone)) {
			nomeSubIcone = "um_pixel";
		}

		this.nomeSubIcone = nomeSubIcone;
		this.subIcone = Icones.getIcon(nomeSubIcone);
	}

	public String getNomeIcone() {
		return nomeIcone;
	}

	public String getNomeSubIcone() {
		return nomeSubIcone;
	}

	public List<Objeto> getObjetos() {
		return objetos;
	}

	public void add(Objeto objeto) {
		if (objeto.pai != null) {
			objeto.pai.excluir(objeto);
		}

		objeto.pai = this;
		objetos.add(objeto);
	}

	public void excluir(Objeto obj) {
		if (obj.pai == this) {
			obj.pai = null;
			objetos.remove(obj);
		}
	}

	public void limpar() {
		while (!estaVazio()) {
			Objeto obj = objetos.get(0);
			excluir(obj);
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
		return titulo + (objetos.size() > 1 ? "(" + objetos.size() + ")" : "");
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

	public String getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public boolean isDesabilitado() {
		return desabilitado;
	}

	public void setDesabilitado(boolean desabilitado) {
		this.desabilitado = desabilitado;
	}

	public boolean isManterVazio() {
		return manterVazio;
	}

	public void setManterVazio(boolean manterVazio) {
		this.manterVazio = manterVazio;
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