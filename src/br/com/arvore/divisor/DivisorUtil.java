package br.com.arvore.divisor;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Objects;

public class DivisorUtil {
	private DivisorUtil() {
	}

	public static Component clonarLeft(Divisor divisor) {
		Component componente = divisor.getLeftComponent();

		if (componente instanceof DivisorClone) {
			return ((DivisorClone) componente).clonar();
		}

		throw new IllegalStateException();
	}

	public static Component clonarRight(Divisor divisor) {
		Component componente = divisor.getRightComponent();

		if (componente instanceof DivisorClone) {
			return ((DivisorClone) componente).clonar();
		}

		throw new IllegalStateException();
	}

	public static void novoHorizontalLeft(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor objeto = criarDivisor(Divisor.HORIZONTAL_SPLIT, left, right);
		divisor.setLeftComponent(objeto);
		objeto.setDividerLocation(d.width / 2);
	}

	public static void novoHorizontalRight(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor objeto = criarDivisor(Divisor.HORIZONTAL_SPLIT, left, right);
		divisor.setRightComponent(objeto);
		objeto.setDividerLocation(d.width / 2);
	}

	public static void novoVerticalLeft(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor objeto = criarDivisor(Divisor.VERTICAL_SPLIT, left, right);
		divisor.setLeftComponent(objeto);
		objeto.setDividerLocation(d.height / 2);
	}

	public static void novoVerticalRight(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor objeto = criarDivisor(Divisor.VERTICAL_SPLIT, left, right);
		divisor.setRightComponent(objeto);
		objeto.setDividerLocation(d.height / 2);
	}

	private static Divisor criarDivisor(int orientacao, Component left, Component right) {
		Divisor objeto = new Divisor();

		objeto.setOrientation(orientacao);
		objeto.setRightComponent(right);
		objeto.setLeftComponent(left);

		return objeto;
	}

	public static void checarValido(Divisor divisor, Component comp) {
		Objects.requireNonNull(comp);

		if (divisor.getLeftComponent() != comp && divisor.getRightComponent() != comp) {
			throw new IllegalStateException();
		}
	}
}