package br.com.arvore;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import br.com.arvore.banco.PersistenciaUtil;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Util;

public class Objeto {
	private String nomeIconeManterVazio;
	private final List<Objeto> objetos;
	private final List<Objeto> nativos;
	private boolean nativoArmazenados;
	private String instrucaoSubTabela;
	private String instrucaoSubUpdate;
	private String instrucaoSubDelete;
	private String instrucaoSubInsert;
	private String instrucaoArvore;
	private String instrucaoTabela;
	private String instrucaoUpdate;
	private String instrucaoDelete;
	private String instrucaoInsert;
	private Icon iconeManterVazio;
	private boolean pesquisaPopup;
	private String subObservacao;
	private String subComentario;
	private boolean desabilitado;
	private String subDescricao;
	private boolean manterVazio;
	private final String titulo;
	private String nomeSubIcone;
	private String observacao;
	private String comentario;
	private String subAlerta;
	private String nomeIcone;
	private String descricao;
	private String alerta;
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

		clone.nomeIconeManterVazio = nomeIconeManterVazio;
		clone.instrucaoSubTabela = instrucaoSubTabela;
		clone.instrucaoSubUpdate = instrucaoSubUpdate;
		clone.instrucaoSubDelete = instrucaoSubDelete;
		clone.instrucaoSubInsert = instrucaoSubInsert;
		clone.iconeManterVazio = iconeManterVazio;
		clone.instrucaoArvore = instrucaoArvore;
		clone.instrucaoTabela = instrucaoTabela;
		clone.instrucaoUpdate = instrucaoUpdate;
		clone.instrucaoDelete = instrucaoDelete;
		clone.instrucaoInsert = instrucaoInsert;
		clone.subObservacao = subObservacao;
		clone.subComentario = subComentario;
		clone.pesquisaPopup = pesquisaPopup;
		clone.subDescricao = subDescricao;
		clone.desabilitado = desabilitado;
		clone.nomeSubIcone = nomeSubIcone;
		clone.manterVazio = manterVazio;
		clone.observacao = observacao;
		clone.comentario = comentario;
		clone.subAlerta = subAlerta;
		clone.descricao = descricao;
		clone.nomeIcone = nomeIcone;
		clone.subIcone = subIcone;
		clone.alerta = alerta;
		clone.icone = icone;

		for (Objeto obj : objetos) {
			clone.add(obj.clonar());
		}

		return clone;
	}

	public void inflar() throws Exception {
		if (Constantes.INFLAR_DESATIVADO) {
			return;
		}

		if (!nativoArmazenados) {
			for (Objeto obj : objetos) {
				nativos.add(obj.clonar());
			}

			nativoArmazenados = true;
		}

		limpar();

		if (Util.estaVazio(instrucaoArvore)) {
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
			List<Objeto> listagem = ObjetoUtil.getObjetos(this);

			Param param = null;

			if (!listagem.isEmpty()) {
				param = criarParam();
			}

			for (Objeto obj : listagem) {
				if (obj.isDesabilitado()) {
					continue;
				}

				atributosSet(obj, param);
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
		if (Constantes.INFLAR_DESATIVADO) {
			return;
		}

		if (!nativoArmazenados) {
			for (Objeto obj : objetos) {
				nativos.add(obj.clonar());
			}

			nativoArmazenados = true;
			inflarParcial2();
		}
	}

	public void inflarParcial2() throws Exception {
		if (Constantes.INFLAR_DESATIVADO) {
			return;
		}

		if (!nativoArmazenados) {
			return;
		}

		limpar();

		if (Util.estaVazio(instrucaoArvore)) {
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
			List<Objeto> listagem = ObjetoUtil.getObjetos(this);

			Param param = null;

			if (!listagem.isEmpty()) {
				param = criarParam();
			}

			for (Objeto obj : listagem) {
				if (obj.isDesabilitado()) {
					continue;
				}

				atributosSet(obj, param);
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

	class Param {
		boolean subTabela;
		int[] ixSubTabela;

		boolean subUpdate;
		int[] ixSubUpdate;

		boolean subDelete;
		int[] ixSubDelete;

		boolean subInsert;
		int[] ixSubInsert;
	}

	Param criarParam() {
		Param param = new Param();

		param.subTabela = !Util.estaVazio(instrucaoSubTabela);
		param.ixSubTabela = PersistenciaUtil.getIndiceParametros(instrucaoSubTabela);

		param.subUpdate = !Util.estaVazio(instrucaoSubUpdate);
		param.ixSubUpdate = PersistenciaUtil.getIndiceParametros(instrucaoSubUpdate);

		param.subDelete = !Util.estaVazio(instrucaoSubDelete);
		param.ixSubDelete = PersistenciaUtil.getIndiceParametros(instrucaoSubDelete);

		param.subInsert = !Util.estaVazio(instrucaoSubInsert);
		param.ixSubInsert = PersistenciaUtil.getIndiceParametros(instrucaoSubInsert);

		return param;
	}

	private void atributosSet(Objeto obj, Param param) {
		obj.setIcone(getSubIcone());

		if (param.subTabela) {
			obj.setInstrucaoTabela(PersistenciaUtil.substituir(instrucaoSubTabela, obj, param.ixSubTabela));
		}

		if (param.subUpdate) {
			obj.setInstrucaoUpdate(PersistenciaUtil.substituir(instrucaoSubUpdate, obj, param.ixSubUpdate));
		}

		if (param.subDelete) {
			obj.setInstrucaoDelete(PersistenciaUtil.substituir(instrucaoSubDelete, obj, param.ixSubDelete));
		}

		if (param.subInsert) {
			obj.setInstrucaoInsert(PersistenciaUtil.substituir(instrucaoSubInsert, obj, param.ixSubInsert));
		}

		obj.setObservacao(subObservacao);
		obj.setComentario(subComentario);
		obj.setDescricao(subDescricao);
		obj.setAlerta(subAlerta);
	}

	public Objeto getPai() {
		return pai;
	}

	public Icon getIcone() {
		return icone;
	}

	public Icon getIconeManterVazio() {
		return iconeManterVazio;
	}

	public Icon getSubIcone() {
		return subIcone;
	}

	public void setIcone(Icon icone) {
		this.icone = icone;
	}

	public void setIconeManterVazio(Icon iconeManterVazio) {
		this.iconeManterVazio = iconeManterVazio;
	}

	public void setSubIcone(Icon subIcone) {
		this.subIcone = subIcone;
	}

	public void setNomeIcone(String nomeIcone) {
		if (Util.estaVazio(nomeIcone)) {
			nomeIcone = Constantes.UM_PIXEL;
		}

		this.nomeIcone = nomeIcone;
		this.icone = Icones.getIcon(nomeIcone);
	}

	public void setNomeSubIcone(String nomeSubIcone) {
		if (Util.estaVazio(nomeSubIcone)) {
			nomeSubIcone = Constantes.UM_PIXEL;
		}

		this.nomeSubIcone = nomeSubIcone;
		this.subIcone = Icones.getIcon(nomeSubIcone);
	}

	public void setNomeIconeManterVazio(String nomeIconeManterVazio) {
		if (Util.estaVazio(nomeIconeManterVazio)) {
			nomeIconeManterVazio = Constantes.UM_PIXEL;
		}

		this.nomeIconeManterVazio = nomeIconeManterVazio;
		this.iconeManterVazio = Icones.getIcon(nomeIconeManterVazio);
	}

	public String getNomeIcone() {
		return nomeIcone;
	}

	public String getNomeIconeManterVazio() {
		return nomeIconeManterVazio;
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
		return (objetos.size() > 1 ? "[" + objetos.size() + "] " : "") + titulo;
	}

	public int getIndice(Objeto objeto) {
		return objetos.indexOf(objeto);
	}

	public Objeto getObjeto(int indice) {
		return objetos.get(indice);
	}

	public String getInstrucaoArvore() {
		return instrucaoArvore;
	}

	public void setInstrucaoArvore(String instrucaoArvore) {
		this.instrucaoArvore = instrucaoArvore;
	}

	public String getInstrucaoTabela() {
		return instrucaoTabela;
	}

	public void setInstrucaoTabela(String instrucaoTabela) {
		this.instrucaoTabela = instrucaoTabela;
	}

	public String getInstrucaoDelete() {
		return instrucaoDelete;
	}

	public void setInstrucaoDelete(String instrucaoDelete) {
		this.instrucaoDelete = instrucaoDelete;
	}

	public String getInstrucaoUpdate() {
		return instrucaoUpdate;
	}

	public void setInstrucaoUpdate(String instrucaoUpdate) {
		this.instrucaoUpdate = instrucaoUpdate;
	}

	public String getInstrucaoInsert() {
		return instrucaoInsert;
	}

	public void setInstrucaoInsert(String instrucaoInsert) {
		this.instrucaoInsert = instrucaoInsert;
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

	public boolean isPesquisaPopup() {
		return pesquisaPopup;
	}

	public void setPesquisaPopup(boolean pesquisaPopup) {
		this.pesquisaPopup = pesquisaPopup;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getInstrucaoSubTabela() {
		return instrucaoSubTabela;
	}

	public void setInstrucaoSubTabela(String instrucaoSubTabela) {
		this.instrucaoSubTabela = instrucaoSubTabela;
	}

	public String getInstrucaoSubUpdate() {
		return instrucaoSubUpdate;
	}

	public void setInstrucaoSubUpdate(String instrucaoSubUpdate) {
		this.instrucaoSubUpdate = instrucaoSubUpdate;
	}

	public String getInstrucaoSubDelete() {
		return instrucaoSubDelete;
	}

	public void setInstrucaoSubDelete(String instrucaoSubDelete) {
		this.instrucaoSubDelete = instrucaoSubDelete;
	}

	public String getInstrucaoSubInsert() {
		return instrucaoSubInsert;
	}

	public void setInstrucaoSubInsert(String instrucaoSubInsert) {
		this.instrucaoSubInsert = instrucaoSubInsert;
	}

	public String getSubObservacao() {
		return subObservacao;
	}

	public void setSubObservacao(String subObservacao) {
		this.subObservacao = subObservacao;
	}

	public String getSubDescricao() {
		return subDescricao;
	}

	public void setSubDescricao(String subDescricao) {
		this.subDescricao = subDescricao;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getAlerta() {
		return alerta;
	}

	public void setAlerta(String alerta) {
		this.alerta = alerta;
	}

	public String getSubComentario() {
		return subComentario;
	}

	public void setSubComentario(String subComentario) {
		this.subComentario = subComentario;
	}

	public String getSubAlerta() {
		return subAlerta;
	}

	public void setSubAlerta(String subAlerta) {
		this.subAlerta = subAlerta;
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