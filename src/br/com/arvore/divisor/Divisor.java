package br.com.arvore.divisor;

import java.awt.Component;

import br.com.arvore.comp.SplitPane;

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
		Component left = leftComponent;
		remove(leftComponent);

		if (disposicao == ESQUERDO) {
			DivisorUtil.novoHorizontalLeft(this, left, clone);
		} else if (disposicao == DIREITO) {
			DivisorUtil.novoHorizontalLeft(this, clone, left);

		} else if (disposicao == ABAIXO) {
			DivisorUtil.novoVerticalLeft(this, clone, left);
		} else if (disposicao == ACIMA) {
			DivisorUtil.novoVerticalLeft(this, left, clone);
		}
	}

	public void clonarRight(byte disposicao) {
		Component clone = DivisorUtil.clonarRight(this);
		Component right = rightComponent;
		remove(rightComponent);

		if (disposicao == ESQUERDO) {
			DivisorUtil.novoHorizontalRight(this, right, clone);
		} else if (disposicao == DIREITO) {
			DivisorUtil.novoHorizontalRight(this, clone, right);

		} else if (disposicao == ABAIXO) {
			DivisorUtil.novoVerticalRight(this, clone, right);
		} else if (disposicao == ACIMA) {
			DivisorUtil.novoVerticalRight(this, right, clone);
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
			DivisorUtil.configDivisor(this, componente);
			DivisorUtil.setLeftTrue(componente);
			setLeftComponent(componente);

		} else if (divisor == rightComponent) {
			DivisorUtil.configDivisor(this, componente);
			DivisorUtil.setLeftFalse(componente);
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
			DivisorUtil.configDivisor(this, componente);
			DivisorUtil.setLeftTrue(componente);
			setLeftComponent(componente);

		} else if (divisor == rightComponent) {
			DivisorUtil.configDivisor(this, componente);
			DivisorUtil.setLeftFalse(componente);
			setRightComponent(componente);

		} else {
			throw new IllegalStateException();
		}
	}
}