package br.com.arvore.divisor;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import br.com.arvore.comp.SplitPane;
import br.com.arvore.container.Container;
import br.com.arvore.controle.Controle;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.fichario.FicharioListener;
import br.com.arvore.util.Layout;
import br.com.arvore.util.Obj;
import br.com.arvore.util.XMLUtil;

public class Divisor extends SplitPane implements Layout {
	private static final long serialVersionUID = 1L;
	private final Listener listener = new Listener();
	private final List<DivisorListener> ouvintes;
	private MementoDivisor mementoDivisor;
	public static final byte ESQUERDO = 1;
	public static final byte DIREITO = 2;
	public static final byte ABAIXO = 3;
	public static final byte ACIMA = 4;

	public Divisor() {
		ouvintes = new ArrayList<>();
	}

	public void adicionarOuvinte(DivisorListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.add(listener);
	}

	public void excluirOuvinte(DivisorListener listener) {
		if (listener == null) {
			return;
		}

		ouvintes.remove(listener);
	}

	@Override
	public void setLeftComponent(Component comp) {
		super.setLeftComponent(comp);

		if (comp instanceof Fichario) {
			Fichario fichario = (Fichario) comp;
			fichario.excluirOuvinte(listener);
			fichario.adicionarOuvinte(listener);
			fichario.setLeft(true);

		} else if (comp instanceof Divisor) {
			Divisor divisor = ((Divisor) comp);
			divisor.excluirOuvinte(listener);
			divisor.adicionarOuvinte(listener);

		} else if (comp instanceof Controle) {
		} else if (comp instanceof JButton) {
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void setRightComponent(Component comp) {
		super.setRightComponent(comp);

		if (comp instanceof Fichario) {
			Fichario fichario = (Fichario) comp;
			fichario.excluirOuvinte(listener);
			fichario.adicionarOuvinte(listener);
			fichario.setLeft(false);

		} else if (comp instanceof Divisor) {
			Divisor divisor = ((Divisor) comp);
			divisor.excluirOuvinte(listener);
			divisor.adicionarOuvinte(listener);

		} else if (comp instanceof Controle) {
		} else if (comp instanceof JButton) {
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void remove(Component comp) {
		super.remove(comp);

		if (comp instanceof Fichario) {
			Fichario fichario = (Fichario) comp;
			fichario.excluirOuvinte(listener);
		} else if (comp instanceof Divisor) {
			Divisor divisor = ((Divisor) comp);
			divisor.excluirOuvinte(listener);
		}
	}

	// public void clonarLeft(byte disposicao) {
	// Component clone = DivisorUtil.clonarLeft(this);
	// Component left = leftComponent;
	// criarMementoDivisor();
	//
	// if (disposicao == ESQUERDO) {
	// DivisorUtil.novoHorizontalLeft(this, left, clone, left.getSize());
	// } else if (disposicao == DIREITO) {
	// DivisorUtil.novoHorizontalLeft(this, clone, left, left.getSize());
	// } else if (disposicao == ABAIXO) {
	// DivisorUtil.novoVerticalLeft(this, clone, left, left.getSize());
	// } else if (disposicao == ACIMA) {
	// DivisorUtil.novoVerticalLeft(this, left, clone, left.getSize());
	// } else {
	// throw new IllegalStateException();
	// }
	//
	// restaurarMementoDivisor();
	// }

	// public void clonarRight(byte disposicao) {
	// Component clone = DivisorUtil.clonarRight(this);
	// Component right = rightComponent;
	// criarMementoDivisor();
	//
	// if (disposicao == ESQUERDO) {
	// DivisorUtil.novoHorizontalRight(this, right, clone, right.getSize());
	// } else if (disposicao == DIREITO) {
	// DivisorUtil.novoHorizontalRight(this, clone, right, right.getSize());
	// } else if (disposicao == ABAIXO) {
	// DivisorUtil.novoVerticalRight(this, clone, right, right.getSize());
	// } else if (disposicao == ACIMA) {
	// DivisorUtil.novoVerticalRight(this, right, clone, right.getSize());
	// } else {
	// throw new IllegalStateException();
	// }
	//
	// restaurarMementoDivisor();
	// }

	public void excluirLeft() {
		remove(leftComponent);
		ouvintes.forEach(o -> o.excluidoLeft(this));
	}

	public void excluirRight() {
		remove(rightComponent);
		ouvintes.forEach(o -> o.excluidoRight(this));
	}

	private class Listener implements DivisorListener, FicharioListener {
		@Override
		public void excluidoLeft(Divisor divisor) {
			DivisorUtil.checarValido(Divisor.this, divisor);
			Component right = divisor.getRightComponent();
			criarMementoDivisor();

			if (leftComponent == divisor) {
				setLeftComponent(right);
				// } else if (rightComponent == divisor) {
				// setRightComponent(right);
			} else {
				throw new IllegalStateException();
			}

			restaurarMementoDivisor();
			DivisorUtil.setDividerLocation(Divisor.this);
		}

		@Override
		public void excluidoRight(Divisor divisor) {
			DivisorUtil.checarValido(Divisor.this, divisor);
			Component left = divisor.getLeftComponent();
			criarMementoDivisor();

			/*
			 * if (leftComponent == divisor) { setLeftComponent(left); } else
			 */ if (rightComponent == divisor) {
				setRightComponent(left);
			} else {
				throw new IllegalStateException();
			}

			restaurarMementoDivisor();
			DivisorUtil.setDividerLocation(Divisor.this);
		}

		@Override
		public void pedidoAtualizarObjeto(Container container) {
		}

		@Override
		public void pedidoDestacarObjeto(Container container) {
		}

		@Override
		public void containerSelecionado(Container container) {
		}

		@Override
		public void pedidoExcluirObjeto(Container container) {
		}

		@Override
		public void selecionadoObjeto(Container container) {
		}

		@Override
		public void containerExcluido(Container container) {
		}
	};

	private void criarMementoDivisor() {
		mementoDivisor = new MementoDivisor(this);
	}

	private void restaurarMementoDivisor() {
		if (mementoDivisor != null) {
			mementoDivisor.restaurar();
		}
	}

	private class MementoDivisor {
		final int localizacao;

		MementoDivisor(Divisor divisor) {
			localizacao = divisor.getDividerLocation();
		}

		void restaurar() {
			setDividerLocation(localizacao);
		}
	}

	public boolean isHorizontal() {
		return orientation == HORIZONTAL_SPLIT;
	}

	@Override
	public void salvarLayout(XMLUtil xml) {
		DivisorUtil.salvarLayout(xml, this);
	}

	@Override
	public void aplicarLayout(Obj obj) {
		DivisorUtil.aplicarLayout(obj, this);
	}

	@Override
	public void ajusteScroll() {
		DivisorUtil.ajusteScroll(this);
	}
}