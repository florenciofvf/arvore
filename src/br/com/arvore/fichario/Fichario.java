package br.com.arvore.fichario;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.ObjetoUtil;
import br.com.arvore.Objeto;
import br.com.arvore.componente.TabbedPane;
import br.com.arvore.container.Container;
import br.com.arvore.container.ContainerListener;
import br.com.arvore.formulario.Formulario;
import br.com.arvore.titulo.Titulo;
import br.com.arvore.titulo.TituloListener;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;
import br.com.arvore.xml.XML;

public class Fichario extends TabbedPane {
	private static final long serialVersionUID = 1L;
	private final List<FicharioListener> ouvintes;
	private final Formulario formulario;
	private Objeto raiz;

	public Fichario(Formulario formulario) {
		this.formulario = formulario;
		ouvintes = new ArrayList<>();
		addChangeListener(e -> abaSelecionada());
	}

	public void adicionarOuvinte(FicharioListener listener) {
		ouvintes.add(listener);
	}

	public void setRaiz(Objeto raiz) {
		this.raiz = raiz;
	}

	public Objeto getRaiz() {
		return raiz;
	}

	private void abaSelecionada() {
		int indice = getSelectedIndex();

		if (indice != -1) {
			Container container = (Container) getComponentAt(indice);
			ouvintes.forEach(o -> o.containerSelecionado(container));
		}
	}

	public void abrirArquivo(File file, boolean msgInexistente, boolean msgSemConteudo, boolean msgNaoLeitura) {
		if (file != null) {
			if (!file.exists()) {
				if (msgInexistente)
					Util.mensagem(formulario,
							Mensagens.getString("erro.arquivo.inexistente") + "\n\n\n" + file.getAbsolutePath());
				return;
			}

			if (file.length() == 0) {
				if (msgSemConteudo)
					Util.mensagem(formulario,
							Mensagens.getString("erro.arquivo.vazio") + "\n\n\n" + file.getAbsolutePath());
				return;
			}

			if (!file.canRead()) {
				if (msgNaoLeitura)
					Util.mensagem(formulario,
							Mensagens.getString("erro.arquivo.leitura") + "\n\n\n" + file.getAbsolutePath());
				return;
			}

			limpar();

			try {
				formulario.setTitle(Mensagens.getString("label.arvore"));
				raiz = XML.processar(file);
				addAba("label.objetos", raiz, true);
				formulario.setTitle(Mensagens.getString("label.arvore") + " - " + file.getAbsolutePath());
				formulario.organizacaoNormal();
			} catch (Exception ex) {
				Util.stackTraceAndMessage("ABRIR ARQUIVO", ex, formulario);
			}
		}
	}

	private void limpar() {
		while (getTabCount() > 0) {
			Container container = (Container) getComponentAt(0);
			notificarContainerExcluido(container);
			remove(0);
		}
	}

	private void notificarContainerExcluido(Container container) {
		ouvintes.forEach(o -> o.containerExcluido(container));
	}

	public void addAba(String chaveTitulo, Objeto objeto, boolean clonar) throws Exception {
		objeto = objeto.clonar();

		ObjetoUtil.inflar(objeto);

		Container container = new Container(objeto);
		container.adicionarOuvinte(containerListener);
		addTab(chaveTitulo, container);

		Titulo titulo = new Titulo(this, clonar);
		titulo.adicionarOuvinte(tituloListener);
		setTabComponentAt(getTabCount() - 1, titulo);
	}

	private TituloListener tituloListener = new TituloListener() {
		@Override
		public void excluirAba(int indice) {
			Container container = (Container) getComponentAt(indice);
			notificarContainerExcluido(container);
			remove(indice);
		}

		@Override
		public void clonarAba() {
			try {
				addAba("label.objetos", raiz, false);
			} catch (Exception ex) {
				Util.stackTraceAndMessage("CLONAR ABA", ex, Fichario.this);
			}
		}
	};

	private ContainerListener containerListener = new ContainerListener() {
		@Override
		public void selecionadoObjeto(Container container) {
			ouvintes.forEach(o -> o.selecionadoObjeto(container));
		}

		@Override
		public void pedidoAtualizarObjeto(Container container) {
			ouvintes.forEach(o -> o.pedidoAtualizarObjeto(container));
		}

		@Override
		public void pedidoDestacarObjeto(Container container) {
			ouvintes.forEach(o -> o.pedidoDestacarObjeto(container));
		}

		@Override
		public void pedidoExcluirObjeto(Container container) {
			ouvintes.forEach(o -> o.pedidoExcluirObjeto(container));
		}
	};
}