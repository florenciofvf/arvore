package br.com.arvore;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import br.com.arvore.banco.PersistenciaUtil;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Util;

public class Objeto {
	private String grupoItensArvore;
	private String grupoItensTabela;
	private String grupoComentario;
	private String grupoObservacao;
	private String grupoDescricao;
	private String grupoInsert;
	private String grupoUpdate;
	private String grupoDelete;
	private String grupoAlerta;

	private String itemObjetoTitulo;
	private String itemObjetoArvore;
	private String itemObjetoTabela;
	private String itemComentario;
	private String itemObservacao;
	private String itemDescricao;
	private String itemTabela;
	private String itemInsert;
	private String itemUpdate;
	private String itemDelete;
	private String itemAlerta;

	private String nomeIconeManterVazio;
	private final List<Objeto> objetos;
	private final List<Objeto> nativos;
	private boolean nativoArmazenados;
	private Icon iconeManterVazio;
	private boolean pesquisaPopup;
	private String nomeItemIcone;
	private boolean desabilitado;
	private boolean manterVazio;
	private final String titulo;
	private String nomeIcone;
	private Icon itemIcone;
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
		clone.iconeManterVazio = iconeManterVazio;

		clone.itemObjetoTitulo = itemObjetoTitulo;
		clone.itemObjetoArvore = itemObjetoArvore;
		clone.itemObjetoTabela = itemObjetoTabela;
		clone.itemComentario = itemComentario;
		clone.itemObservacao = itemObservacao;
		clone.itemDescricao = itemDescricao;
		clone.itemTabela = itemTabela;
		clone.itemInsert = itemInsert;
		clone.itemUpdate = itemUpdate;
		clone.itemDelete = itemDelete;
		clone.itemAlerta = itemAlerta;

		clone.grupoItensArvore = grupoItensArvore;
		clone.grupoItensTabela = grupoItensTabela;
		clone.grupoComentario = grupoComentario;
		clone.grupoObservacao = grupoObservacao;
		clone.grupoDescricao = grupoDescricao;
		clone.grupoInsert = grupoInsert;
		clone.grupoUpdate = grupoUpdate;
		clone.grupoDelete = grupoDelete;
		clone.grupoAlerta = grupoAlerta;

		clone.pesquisaPopup = pesquisaPopup;
		clone.nomeItemIcone = nomeItemIcone;
		clone.desabilitado = desabilitado;
		clone.manterVazio = manterVazio;
		clone.nomeIcone = nomeIcone;
		clone.itemIcone = itemIcone;
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

		if (Util.estaVazio(grupoItensArvore)) {
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

		if (Util.estaVazio(grupoItensArvore)) {
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
		boolean itemObjetoTitulo;

		boolean itemObjetoArvore;
		int[] ixItemObjetoArvore;

		boolean itemObjetoTabela;
		int[] ixItemObjetoTabela;

		boolean itemTabela;
		int[] ixItemTabela;

		boolean itemInsert;
		int[] ixItemInsert;

		boolean itemUpdate;
		int[] ixItemUpdate;

		boolean itemDelete;
		int[] ixItemDelete;
	}

	Param criarParam() {
		Param param = new Param();

		param.itemObjetoTitulo = !Util.estaVazio(itemObjetoTitulo);

		param.itemObjetoArvore = !Util.estaVazio(itemObjetoArvore);
		param.ixItemObjetoArvore = PersistenciaUtil.getIndiceParametros(itemObjetoArvore);

		param.itemObjetoTabela = !Util.estaVazio(itemObjetoTabela);
		param.ixItemObjetoTabela = PersistenciaUtil.getIndiceParametros(itemObjetoTabela);

		param.itemTabela = !Util.estaVazio(itemTabela);
		param.ixItemTabela = PersistenciaUtil.getIndiceParametros(itemTabela);

		param.itemInsert = !Util.estaVazio(itemInsert);
		param.ixItemInsert = PersistenciaUtil.getIndiceParametros(itemInsert);

		param.itemUpdate = !Util.estaVazio(itemUpdate);
		param.ixItemUpdate = PersistenciaUtil.getIndiceParametros(itemUpdate);

		param.itemDelete = !Util.estaVazio(itemDelete);
		param.ixItemDelete = PersistenciaUtil.getIndiceParametros(itemDelete);

		return param;
	}

	private void atributosSet(Objeto obj, Param param) throws Exception {
		obj.setIcone(getItemIcone());

		if (param.itemObjetoTitulo) {
			Objeto auto = new Objeto(itemObjetoTitulo);

			auto.setItemObjetoTitulo(itemObjetoTitulo);
			auto.setItemObjetoArvore(itemObjetoArvore);
			auto.setItemObjetoTabela(itemObjetoTabela);

			auto.setGrupoItensArvore(itemObjetoArvore);
			auto.setGrupoItensTabela(itemObjetoTabela);

			obj.add(auto);

			if (Constantes.INFLAR_ANTECIPADO) {
				auto.inflar();

				if (auto.estaVazio() && !auto.isManterVazio()) {
					obj.excluir(auto);
				}
			}
		}

		if (param.itemTabela) {
			obj.setGrupoItensTabela(PersistenciaUtil.substituir(itemTabela, obj, param.ixItemTabela));
		}

		if (param.itemInsert) {
			obj.setGrupoInsert(PersistenciaUtil.substituir(itemInsert, obj, param.ixItemInsert));
		}

		if (param.itemUpdate) {
			obj.setGrupoUpdate(PersistenciaUtil.substituir(itemUpdate, obj, param.ixItemUpdate));
		}

		if (param.itemDelete) {
			obj.setGrupoDelete(PersistenciaUtil.substituir(itemDelete, obj, param.ixItemDelete));
		}

		obj.setGrupoComentario(itemComentario);
		obj.setGrupoObservacao(itemObservacao);
		obj.setGrupoDescricao(itemDescricao);
		obj.setGrupoAlerta(itemAlerta);
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

	public Icon getItemIcone() {
		return itemIcone;
	}

	public void setIcone(Icon icone) {
		this.icone = icone;
	}

	public void setIconeManterVazio(Icon iconeManterVazio) {
		this.iconeManterVazio = iconeManterVazio;
	}

	public void setItemIcone(Icon itemIcone) {
		this.itemIcone = itemIcone;
	}

	public void setNomeIcone(String nomeIcone) {
		if (Util.estaVazio(nomeIcone)) {
			nomeIcone = Constantes.UM_PIXEL;
		}

		this.nomeIcone = nomeIcone;
		this.icone = Icones.getIcon(nomeIcone);
	}

	public void setNomeItemIcone(String nomeItemIcone) {
		if (Util.estaVazio(nomeItemIcone)) {
			nomeItemIcone = Constantes.UM_PIXEL;
		}

		this.nomeItemIcone = nomeItemIcone;
		this.itemIcone = Icones.getIcon(nomeItemIcone);
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

	public String getNomeItemIcone() {
		return nomeItemIcone;
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

	public String getGrupoItensArvore() {
		return grupoItensArvore;
	}

	public void setGrupoItensArvore(String grupoItensArvore) {
		this.grupoItensArvore = grupoItensArvore;
	}

	public String getGrupoItensTabela() {
		return grupoItensTabela;
	}

	public void setGrupoItensTabela(String grupoItensTabela) {
		this.grupoItensTabela = grupoItensTabela;
	}

	public String getGrupoComentario() {
		return grupoComentario;
	}

	public void setGrupoComentario(String grupoComentario) {
		this.grupoComentario = grupoComentario;
	}

	public String getGrupoObservacao() {
		return grupoObservacao;
	}

	public void setGrupoObservacao(String grupoObservacao) {
		this.grupoObservacao = grupoObservacao;
	}

	public String getGrupoDescricao() {
		return grupoDescricao;
	}

	public void setGrupoDescricao(String grupoDescricao) {
		this.grupoDescricao = grupoDescricao;
	}

	public String getGrupoInsert() {
		return grupoInsert;
	}

	public void setGrupoInsert(String grupoInsert) {
		this.grupoInsert = grupoInsert;
	}

	public String getGrupoUpdate() {
		return grupoUpdate;
	}

	public void setGrupoUpdate(String grupoUpdate) {
		this.grupoUpdate = grupoUpdate;
	}

	public String getGrupoDelete() {
		return grupoDelete;
	}

	public void setGrupoDelete(String grupoDelete) {
		this.grupoDelete = grupoDelete;
	}

	public String getGrupoAlerta() {
		return grupoAlerta;
	}

	public void setGrupoAlerta(String grupoAlerta) {
		this.grupoAlerta = grupoAlerta;
	}

	public String getItemObjetoTitulo() {
		return itemObjetoTitulo;
	}

	public void setItemObjetoTitulo(String itemObjetoTitulo) {
		this.itemObjetoTitulo = itemObjetoTitulo;
	}

	public String getItemObjetoArvore() {
		return itemObjetoArvore;
	}

	public void setItemObjetoArvore(String itemObjetoArvore) {
		this.itemObjetoArvore = itemObjetoArvore;
	}

	public String getItemObjetoTabela() {
		return itemObjetoTabela;
	}

	public void setItemObjetoTabela(String itemObjetoTabela) {
		this.itemObjetoTabela = itemObjetoTabela;
	}

	public String getItemComentario() {
		return itemComentario;
	}

	public void setItemComentario(String itemComentario) {
		this.itemComentario = itemComentario;
	}

	public String getItemObservacao() {
		return itemObservacao;
	}

	public void setItemObservacao(String itemObservacao) {
		this.itemObservacao = itemObservacao;
	}

	public String getItemDescricao() {
		return itemDescricao;
	}

	public void setItemDescricao(String itemDescricao) {
		this.itemDescricao = itemDescricao;
	}

	public String getItemTabela() {
		return itemTabela;
	}

	public void setItemTabela(String itemTabela) {
		this.itemTabela = itemTabela;
	}

	public String getItemInsert() {
		return itemInsert;
	}

	public void setItemInsert(String itemInsert) {
		this.itemInsert = itemInsert;
	}

	public String getItemUpdate() {
		return itemUpdate;
	}

	public void setItemUpdate(String itemUpdate) {
		this.itemUpdate = itemUpdate;
	}

	public String getItemDelete() {
		return itemDelete;
	}

	public void setItemDelete(String itemDelete) {
		this.itemDelete = itemDelete;
	}

	public String getItemAlerta() {
		return itemAlerta;
	}

	public void setItemAlerta(String itemAlerta) {
		this.itemAlerta = itemAlerta;
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