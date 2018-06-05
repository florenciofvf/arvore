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

	public void clonarComponente(Clonavel componente, byte local) {
		if (componente == null) {
			return;
		}

		if (componente != leftComponent && componente != rightComponent) {
			throw new IllegalStateException();
		}

		Clonavel clone = componente.clonar();
		Divisor horizontal = null;
		Divisor vertical = null;

		if (local == ESQUERDO) {
			if (componente == leftComponent) {
				horizontal = criarDivisorHorizontal((Component) clone, leftComponent);
				setLeftComponent(horizontal);
			} else if (componente == rightComponent) {
				horizontal = criarDivisorHorizontal((Component) clone, leftComponent);
				setLeftComponent(horizontal);
			}
		} else if (local == DIREITO) {
			if (componente == leftComponent) {

			} else if (componente == leftComponent) {

			}
		} else if (local == ABAIXO) {
			if (componente == leftComponent) {

			} else if (componente == leftComponent) {

			}
		} else if (local == ACIMA) {
			if (componente == leftComponent) {

			} else if (componente == leftComponent) {

			}
		}
	}

	private Divisor criarDivisorVertical(Component acima, Component abaixo) {
		return criarDivisor(VERTICAL_SPLIT, acima, abaixo);
	}

	private Divisor criarDivisorHorizontal(Component esquerdo, Component direito) {
		return criarDivisor(HORIZONTAL_SPLIT, esquerdo, direito);
	}

	private Divisor criarDivisor(int orientacao, Component c1, Component c2) {
		Divisor divisor = new Divisor();

		divisor.setOrientation(orientacao);
		divisor.setRightComponent(c1);
		divisor.setLeftComponent(c2);

		return divisor;
	}

	public void excluirComponente(Component componente) {
		if (componente == null || divisorListener == null) {
			return;
		}

		if (componente == leftComponent) {
			excluirLeftComponente();
		} else if (componente == leftComponent) {
			excluirRightComponente();
		}
	}

	private void excluirLeftComponente() {
		remove(leftComponent);
		divisorListener.excluidoComponente(this, leftComponent);
	}

	private void excluirRightComponente() {
		remove(rightComponent);
		divisorListener.excluidoComponente(this, rightComponent);
	}

	@Override
	public void excluidoComponente(Divisor divisor, Component componente) {
		// TODO Auto-generated method stub
	}
}