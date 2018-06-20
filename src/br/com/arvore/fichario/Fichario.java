package br.com.arvore.fichario;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.Objeto;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.container.Container;
import br.com.arvore.container.ContainerListener;
import br.com.arvore.divisor.DivisorClone;
import br.com.arvore.util.Layout;
import br.com.arvore.util.Obj;
import br.com.arvore.util.Util;
import br.com.arvore.util.XMLUtil;

public class Fichario extends TabbedPane implements DivisorClone, Layout {
	private static final long serialVersionUID = 1L;
	private final List<FicharioListener> ouvintes;
	private final Objeto raiz;
	private boolean left;

	public Fichario(Objeto raiz) {
		addChangeListener(e -> abaSelecionada());
		ouvintes = new ArrayList<>();
		this.raiz = raiz;
	}

	public void adicionarOuvinte(FicharioListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.add(listener);
	}

	public void excluirOuvinte(FicharioListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.remove(listener);
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
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

	public void adicionarAba(boolean principal) throws Exception {
		Container container = new Container(raiz.clonar());
		container.adicionarOuvinte(containerListener);
		addTab("label.objetos", container);

		Titulo titulo = new Titulo(this, principal);
		titulo.adicionarOuvinte(tituloListener);
		setTabComponentAt(getTabCount() - 1, titulo);

		setDividerLocation(getTabCount() - 1);
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
		FicharioUtil.aplicarLayout(obj, this);
	}

	protected TituloListener tituloListener = new TituloListener() {
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
				adicionarAba(false);
			} catch (Exception ex) {
				Util.stackTraceAndMessage("CLONAR ABA", ex, Fichario.this);
			}
		}

		@Override
		public void excluirFichario() {
			// if (divisor != null && left) {
			// divisor.excluirLeft();
			// } else if (divisor != null && !left) {
			// divisor.excluirRight();
			// }
		}

		@Override
		public void cloneEsquerdo() {
			// if (divisor != null && left) {
			// divisor.clonarLeft(Divisor.ESQUERDO);
			// } else if (divisor != null && !left) {
			// divisor.clonarRight(Divisor.ESQUERDO);
			// } else if (containerLayout != null) {
			// containerLayout.clonarLeft();
			// }
		}

		@Override
		public void cloneDireito() {
			// if (divisor != null && left) {
			// divisor.clonarLeft(Divisor.DIREITO);
			// } else if (divisor != null && !left) {
			// divisor.clonarRight(Divisor.DIREITO);
			// } else if (containerLayout != null) {
			// containerLayout.clonarRight();
			// }
		}

		@Override
		public void cloneAbaixo() {
			// if (divisor != null && left) {
			// divisor.clonarLeft(Divisor.ABAIXO);
			// } else if (divisor != null && !left) {
			// divisor.clonarRight(Divisor.ABAIXO);
			// } else if (containerLayout != null) {
			// containerLayout.clonarAbaixo();
			// }
		}

		@Override
		public void cloneAcima() {
			// if (divisor != null && left) {
			// divisor.clonarLeft(Divisor.ACIMA);
			// } else if (divisor != null && !left) {
			// divisor.clonarRight(Divisor.ACIMA);
			// } else if (containerLayout != null) {
			// containerLayout.clonarAcima();
			// }
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
		Fichario fichario = new Fichario(raiz);

		try {
			fichario.adicionarAba(true);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("CRIAR CLONE", ex, this);
		}

		return fichario;
	}
}