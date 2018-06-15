package br.com.arvore.fichario;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.Objeto;
import br.com.arvore.ObjetoUtil;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.container.Container;
import br.com.arvore.container.ContainerListener;
import br.com.arvore.divisor.Divisor;
import br.com.arvore.divisor.DivisorClone;
import br.com.arvore.formulario.Formulario;
import br.com.arvore.titulo.Titulo;
import br.com.arvore.titulo.TituloListener;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Layout;
import br.com.arvore.util.Obj;
import br.com.arvore.util.Util;
import br.com.arvore.util.XMLUtil;

public class Fichario extends TabbedPane implements DivisorClone, Layout {
	private static final long serialVersionUID = 1L;
	private final List<FicharioListener> ouvintes;
	private final Formulario formulario;
	private final Objeto raiz;
	private Divisor divisor;
	private boolean left;

	public Fichario(Formulario formulario, Objeto raiz) {
		addChangeListener(e -> abaSelecionada());
		this.formulario = formulario;
		ouvintes = new ArrayList<>();
		this.raiz = raiz;
	}

	public void adicionarOuvinte(FicharioListener listener) {
		ouvintes.add(listener);
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public Divisor getDivisor() {
		return divisor;
	}

	public void setDivisor(Divisor divisor) {
		this.divisor = divisor;
	}

	private void abaSelecionada() {
		int indice = getSelectedIndex();

		if (indice != -1) {
			Container container = (Container) getComponentAt(indice);
			ouvintes.forEach(o -> o.containerSelecionado(container));
		}
	}

	public void setDividerLocation(int indice) {
		Container container = (Container) getComponentAt(indice);
		container.setDividerLocation(getWidth() / 2);
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

		setDividerLocation(getTabCount() - 1);
	}

	private void illegalStateException() {
		throw new IllegalStateException();
	}

	@Override
	public void salvarLayout(XMLUtil xml) {
		FicharioUtil.salvarLayout(xml, this);
	}

	public void ajusteScroll() {
		FicharioUtil.ajusteScroll(this);
	}

	@Override
	public void aplicarLayout(Obj obj) {
		if (obj.isLeft()) {
			Obj filho = obj.getFilho(0);

			if (filho.isFichario()) {
				String abas = filho.getValorAtributo(Constantes.ABAS);
				int total = Integer.parseInt(abas);

				for (int i = 1; i < total; i++) {
					tituloListener.clonarAba();
				}

				for (int i = 0; i < total; i++) {
					String titulo = filho.getFilho(i).getValorAtributo(Constantes.TITULO);
					Container container = (Container) getComponentAt(i);
					container.aplicarLayout(filho.getFilho(i));
					setTitleAt(i, titulo);
				}
			} else if (filho.isDivisor()) {
				String orientacao = filho.getValorAtributo(Constantes.ORIENTACAO);

				if (Constantes.VERTICAL.equals(orientacao)) {
					tituloListener.cloneAcima();
					divisor.aplicarLayout(filho);
				} else if (Constantes.HORIZONTAL.equals(orientacao)) {
					tituloListener.cloneDireito();
					divisor.aplicarLayout(filho);
				} else {
					illegalStateException();
				}
			} else {
				illegalStateException();
			}

		} else if (obj.isRight()) {
			Obj filho = obj.getFilho(0);

			if (filho.isFichario()) {
				String abas = filho.getValorAtributo(Constantes.ABAS);
				int total = Integer.parseInt(abas);

				for (int i = 1; i < total; i++) {
					tituloListener.clonarAba();
				}

				for (int i = 0; i < total; i++) {
					String titulo = filho.getFilho(i).getValorAtributo(Constantes.TITULO);
					Container container = (Container) getComponentAt(i);
					container.aplicarLayout(filho.getFilho(i));
					setTitleAt(i, titulo);
				}
			} else if (filho.isDivisor()) {
				String orientacao = filho.getValorAtributo(Constantes.ORIENTACAO);

				if (Constantes.VERTICAL.equals(orientacao)) {
					tituloListener.cloneAcima();
					divisor.aplicarLayout(filho);
				} else if (Constantes.HORIZONTAL.equals(orientacao)) {
					tituloListener.cloneDireito();
					divisor.aplicarLayout(filho);
				} else {
					illegalStateException();
				}
			} else {
				illegalStateException();
			}

		} else {
			illegalStateException();
		}
	}

	private TituloListener tituloListener = new TituloListener() {
		public void selecionarObjeto() {
			abaSelecionada();
		};

		public void clonarLocalDivisor() {
			Container container = (Container) getComponentAt(0);
			int localizacao = container.getDividerLocation();

			for (int i = 1; i < getTabCount(); i++) {
				Container c = (Container) getComponentAt(i);
				c.setDividerLocation(localizacao);
			}
		};

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

		@Override
		public void excluirFichario() {
			if (divisor != null && left) {
				divisor.excluirLeft();
			} else if (divisor != null && !left) {
				divisor.excluirRight();
			}
		}

		@Override
		public void cloneEsquerdo() {
			if (divisor != null && left) {
				divisor.clonarLeft(Divisor.ESQUERDO);
			} else if (divisor != null && !left) {
				divisor.clonarRight(Divisor.ESQUERDO);
			}
		}

		@Override
		public void cloneDireito() {
			if (divisor != null && left) {
				divisor.clonarLeft(Divisor.DIREITO);
			} else if (divisor != null && !left) {
				divisor.clonarRight(Divisor.DIREITO);
			}
		}

		@Override
		public void cloneAbaixo() {
			if (divisor != null && left) {
				divisor.clonarLeft(Divisor.ABAIXO);
			} else if (divisor != null && !left) {
				divisor.clonarRight(Divisor.ABAIXO);
			}
		}

		@Override
		public void cloneAcima() {
			if (divisor != null && left) {
				divisor.clonarLeft(Divisor.ACIMA);
			} else if (divisor != null && !left) {
				divisor.clonarRight(Divisor.ACIMA);
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

	@Override
	public Component clonar() {
		Fichario fichario = new Fichario(formulario, raiz);
		fichario.adicionarOuvinte(formulario.getFicharioListener());

		try {
			fichario.addAba("label.objetos", raiz, true);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("CRIAR ESPELHO", ex, formulario);
		}

		return fichario;
	}
}