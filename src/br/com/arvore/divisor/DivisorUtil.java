package br.com.arvore.divisor;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Objects;

public class DivisorUtil {
	private DivisorUtil() {
	}

	public static Component clonarLeft(Divisor divisor, Dimension dimension) {
		Component componente = divisor.getLeftComponent();

		if (componente instanceof DivisorClone) {
			return ((DivisorClone) componente).clonar(dimension);
		}

		throw new IllegalStateException();
	}

	public static Component clonarRight(Divisor divisor, Dimension dimension) {
		Component componente = divisor.getRightComponent();

		if (componente instanceof DivisorClone) {
			return ((DivisorClone) componente).clonar(dimension);
		}

		throw new IllegalStateException();
	}

	public static void novoHorizontalLeft(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor divisorLeft = criarDivisor(Divisor.HORIZONTAL_SPLIT, left, right);
		divisor.setLeftComponent(divisorLeft);
		divisorLeft.setDividerLocation(d.width / 2);
	}

	public static void novoHorizontalRight(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor divisorRight = criarDivisor(Divisor.HORIZONTAL_SPLIT, left, right);
		divisor.setRightComponent(divisorRight);
		divisorRight.setDividerLocation(d.width / 2);
	}

	public static void novoVerticalLeft(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor divisorLeft = criarDivisor(Divisor.VERTICAL_SPLIT, left, right);
		divisor.setLeftComponent(divisorLeft);
		divisorLeft.setDividerLocation(d.height / 2);
	}

	public static void novoVerticalRight(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor divisorRight = criarDivisor(Divisor.VERTICAL_SPLIT, left, right);
		divisor.setRightComponent(divisorRight);
		divisorRight.setDividerLocation(d.height / 2);
	}

	private static Divisor criarDivisor(int orientacao, Component left, Component right) {
		Divisor divisor = new Divisor();

		divisor.setOrientation(orientacao);
		divisor.setRightComponent(right);
		divisor.setLeftComponent(left);

		return divisor;
	}

	public static void checarValido(Divisor divisor, Component comp) {
		Objects.requireNonNull(comp);

		if (divisor.getLeftComponent() != comp && divisor.getRightComponent() != comp) {
			throw new IllegalStateException();
		}
	}
}