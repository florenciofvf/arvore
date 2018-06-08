package br.com.arvore.divisor;

import java.awt.Component;

import javax.swing.JButton;

import br.com.arvore.comp.SplitPane;
import br.com.arvore.controle.Controle;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.formulario.Formulario;

public class Divisor extends SplitPane {
	private static final long serialVersionUID = 1L;
	private MementoDivisor mementoDivisor;
	public static final byte ESQUERDO = 1;
	public static final byte DIREITO = 2;
	public static final byte ABAIXO = 3;
	public static final byte ACIMA = 4;
	private DivisorListener ouvinte;

	@Override
	public void setLeftComponent(Component comp) {
		super.setLeftComponent(comp);

		if (comp instanceof Fichario) {
			Fichario fichario = (Fichario) comp;
			fichario.setDivisor(this);
			fichario.setLeft(true);
		} else if (comp instanceof Divisor) {
			((Divisor) comp).ouvinte = divisorListener;
		} else if (comp instanceof Formulario.Pnl_padrao) {
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
			fichario.setDivisor(this);
			fichario.setLeft(false);
		} else if (comp instanceof Divisor) {
			((Divisor) comp).ouvinte = divisorListener;
		} else if (comp instanceof Formulario.Pnl_padrao) {
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
			fichario.setDivisor(null);
		} else if (comp instanceof Divisor) {
			((Divisor) comp).ouvinte = null;
		}
	}

	public void clonarLeft(byte disposicao) {
		Component clone = DivisorUtil.clonarLeft(this);
		Component left = leftComponent;
		criarMementoDivisor();

		switch (disposicao) {
		case ESQUERDO:
			DivisorUtil.novoHorizontalLeft(this, left, clone, left.getSize());
			break;
		case DIREITO:
			DivisorUtil.novoHorizontalLeft(this, clone, left, left.getSize());
			break;
		case ABAIXO:
			DivisorUtil.novoVerticalLeft(this, clone, left, left.getSize());
			break;
		case ACIMA:
			DivisorUtil.novoVerticalLeft(this, left, clone, left.getSize());
			break;
		default:
			throw new IllegalStateException();
		}

		restaurarMementoDivisor();
	}

	public void clonarRight(byte disposicao) {
		Component clone = DivisorUtil.clonarRight(this);
		Component right = rightComponent;
		criarMementoDivisor();

		switch (disposicao) {
		case ESQUERDO:
			DivisorUtil.novoHorizontalRight(this, right, clone, right.getSize());
			break;
		case DIREITO:
			DivisorUtil.novoHorizontalRight(this, clone, right, right.getSize());
			break;
		case ABAIXO:
			DivisorUtil.novoVerticalRight(this, clone, right, right.getSize());
			break;
		case ACIMA:
			DivisorUtil.novoVerticalRight(this, right, clone, right.getSize());
			break;
		default:
			throw new IllegalStateException();
		}

		restaurarMementoDivisor();
	}

	public void excluirLeft() {
		if (ouvinte != null) {
			remove(leftComponent);
			ouvinte.excluidoLeft(this);
		}
	}

	public void excluirRight() {
		if (ouvinte != null) {
			remove(rightComponent);
			ouvinte.excluidoRight(this);
		}
	}

	private DivisorListener divisorListener = new DivisorListener() {
		@Override
		public void excluidoLeft(Divisor divisor) {
			DivisorUtil.checarValido(Divisor.this, divisor);
			Component comp = divisor.getRightComponent();
			criarMementoDivisor();

			if (divisor == leftComponent) {
				setLeftComponent(comp);
			} else if (divisor == rightComponent) {
				setRightComponent(comp);
			} else {
				throw new IllegalStateException();
			}

			restaurarMementoDivisor();
			DivisorUtil.setDividerLocation(Divisor.this);
		}

		@Override
		public void excluidoRight(Divisor divisor) {
			DivisorUtil.checarValido(Divisor.this, divisor);
			Component comp = divisor.getLeftComponent();
			criarMementoDivisor();

			if (divisor == leftComponent) {
				setLeftComponent(comp);
			} else if (divisor == rightComponent) {
				setRightComponent(comp);
			} else {
				throw new IllegalStateException();
			}

			restaurarMementoDivisor();
			DivisorUtil.setDividerLocation(Divisor.this);
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
}