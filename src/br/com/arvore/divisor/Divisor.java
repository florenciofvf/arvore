package br.com.arvore.divisor;

import java.awt.Component;

import br.com.arvore.compnte.SplitPane;

public class Divisor extends SplitPane {
	private static final long serialVersionUID = 1L;
	private DivisorListener divisorListener;
	public static final byte ESQUERDO = 1;
	public static final byte DIREITO = 2;
	public static final byte ABAIXO = 3;
	public static final byte ACIMA = 4;

	public void clonarComponente(Component componente, byte local) {
		if (local == ESQUERDO) {

		} else if (local == DIREITO) {

		} else if (local == ABAIXO) {

		} else if (local == ACIMA) {

		}
	}

	public void excluir(Component componente) {
		if (divisorListener != null) {
			divisorListener.excluidoComponente(componente);
		}
	}
}