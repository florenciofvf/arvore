package br.com.arvore.divisor;

import java.awt.Component;
import java.util.Objects;

public class DivisorUtil {
	private DivisorUtil() {
	}

	public static Component clonarLeft(Divisor divisor) {
		Component componente = divisor.getLeftComponent();

		if (componente instanceof DivisorClone) {
			return ((DivisorClone) componente).clonar();
		}

		return null;
	}

	public static Component clonarRight(Divisor divisor) {
		Component componente = divisor.getRightComponent();

		if (componente instanceof DivisorClone) {
			return ((DivisorClone) componente).clonar();
		}

		return null;
	}

	public static void novoHorizontalLeft(Divisor divisor, Component c1, Component c2) {
		divisor.setLeftComponent(criarDivisor(divisor, Divisor.HORIZONTAL_SPLIT, c1, c2));
	}

	public static void novoHorizontalRight(Divisor divisor, Component c1, Component c2) {
		divisor.setRightComponent(criarDivisor(divisor, Divisor.HORIZONTAL_SPLIT, c1, c2));
	}

	public static void novoVerticalLeft(Divisor divisor, Component c1, Component c2) {
		divisor.setLeftComponent(criarDivisor(divisor, Divisor.VERTICAL_SPLIT, c1, c2));
	}

	public static void novoVerticalRight(Divisor divisor, Component c1, Component c2) {
		divisor.setRightComponent(criarDivisor(divisor, Divisor.VERTICAL_SPLIT, c1, c2));
	}

	private static Divisor criarDivisor(Divisor divisor, int orientacao, Component c1, Component c2) {
		Divisor objeto = new Divisor();

		objeto.setOrientation(orientacao);
		objeto.setDivisorListener(divisor);
		objeto.setRightComponent(c2);
		objeto.setLeftComponent(c1);

		return objeto;
	}

	public static void checarValido(Divisor divisor, Component componente) {
		Objects.requireNonNull(componente);

		if (divisor.getLeftComponent() != componente && divisor.getRightComponent() != componente) {
			throw new IllegalStateException();
		}
	}
}