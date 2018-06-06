package br.com.arvore.divisor;

import java.awt.Component;

import br.com.arvore.compnte.SplitPane;

public class Divisor extends SplitPane implements DivisorListener {
	private static final long serialVersionUID = 1L;
	private DivisorListener divisorListener;
	public static final byte ESQUERDO = 1;
	public static final byte DIREITO = 2;
	public static final byte ABAIXO = 3;
	public static final byte ACIMA = 4;

	public void setDivisorListener(DivisorListener divisorListener) {
		this.divisorListener = divisorListener;
	}

	public void clonarLeft(byte disposicao) {
		Component clone = DivisorUtil.clonarLeft(this);

		if (disposicao == ESQUERDO) {
			DivisorUtil.novoHorizontalLeft(this, leftComponent, clone);
		} else if (disposicao == DIREITO) {
			DivisorUtil.novoHorizontalLeft(this, clone, leftComponent);

		} else if (disposicao == ABAIXO) {
			DivisorUtil.novoVerticalLeft(this, clone, leftComponent);
		} else if (disposicao == ACIMA) {
			DivisorUtil.novoVerticalLeft(this, leftComponent, clone);
		}
	}

	public void clonarRight(byte disposicao) {
		Component clone = DivisorUtil.clonarRight(this);

		if (disposicao == ESQUERDO) {
			DivisorUtil.novoHorizontalRight(this, rightComponent, clone);
		} else if (disposicao == DIREITO) {
			DivisorUtil.novoHorizontalRight(this, clone, rightComponent);

		} else if (disposicao == ABAIXO) {
			DivisorUtil.novoVerticalRight(this, clone, rightComponent);
		} else if (disposicao == ACIMA) {
			DivisorUtil.novoVerticalRight(this, rightComponent, clone);
		}
	}

	public void excluirLeft() {
		remove(leftComponent);

		if (divisorListener != null) {
			divisorListener.excluidoLeft(this);
		}
	}

	public void excluirRight() {
		remove(rightComponent);

		if (divisorListener != null) {
			divisorListener.excluidoRight(this);
		}
	}

	@Override
	public void excluidoLeft(Divisor divisor) {
		DivisorUtil.checarValido(this, divisor);
		Component componente = divisor.getRightComponent();

		if (divisor == leftComponent) {
			setLeftComponent(componente);
		} else if (divisor == rightComponent) {
			setRightComponent(componente);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void excluidoRight(Divisor divisor) {
		DivisorUtil.checarValido(this, divisor);
		Component componente = divisor.getLeftComponent();

		if (divisor == leftComponent) {
			setLeftComponent(componente);
		} else if (divisor == rightComponent) {
			setRightComponent(componente);
		} else {
			throw new IllegalStateException();
		}
	}
}