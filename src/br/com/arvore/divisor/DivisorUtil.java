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
		Divisor objeto = criarDivisor(divisor, Divisor.HORIZONTAL_SPLIT, c1, c2);
		divisor.setLeftComponent(objeto);
		objeto.setDividerLocation(divisor.getWidth() / 2);
	}

	public static void novoHorizontalRight(Divisor divisor, Component c1, Component c2) {
		Divisor objeto = criarDivisor(divisor, Divisor.HORIZONTAL_SPLIT, c1, c2);
		divisor.setRightComponent(objeto);
		objeto.setDividerLocation(divisor.getWidth() / 2);
	}

	public static void novoVerticalLeft(Divisor divisor, Component c1, Component c2) {
		Divisor objeto = criarDivisor(divisor, Divisor.VERTICAL_SPLIT, c1, c2);
		divisor.setLeftComponent(objeto);
		objeto.setDividerLocation(divisor.getHeight() / 2);
	}

	public static void novoVerticalRight(Divisor divisor, Component c1, Component c2) {
		Divisor objeto = criarDivisor(divisor, Divisor.VERTICAL_SPLIT, c1, c2);
		divisor.setRightComponent(objeto);
		objeto.setDividerLocation(divisor.getHeight() / 2);
	}

	private static Divisor criarDivisor(DivisorListener listener, int orientacao, Component c1, Component c2) {
		Divisor objeto = new Divisor();

		objeto.setDivisorListener(listener);
		objeto.setOrientation(orientacao);
		objeto.setRightComponent(c2);
		objeto.setLeftComponent(c1);
		configDivisor(objeto, c1);
		configDivisor(objeto, c2);
		setLeftFalse(c2);
		setLeftTrue(c1);

		return objeto;
	}

	public static void configDivisor(Divisor divisor, Component componente) {
		if (componente instanceof DivisorClone) {
			((DivisorClone) componente).setDivisor(divisor);
		}
	}

	public static void setLeftFalse(Component componente) {
		if (componente instanceof DivisorClone) {
			((DivisorClone) componente).setLeft(false);
		}
	}

	public static void setLeftTrue(Component componente) {
		if (componente instanceof DivisorClone) {
			((DivisorClone) componente).setLeft(true);
		}
	}

	public static void checarValido(Divisor divisor, Component componente) {
		Objects.requireNonNull(componente);

		if (divisor.getLeftComponent() != componente && divisor.getRightComponent() != componente) {
			throw new IllegalStateException();
		}
	}
}