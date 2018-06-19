package br.com.arvore.divisor;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Objects;

import javax.swing.SwingUtilities;

import br.com.arvore.controle.Controle;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Layout;
import br.com.arvore.util.Obj;
import br.com.arvore.util.XMLUtil;

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
		Divisor divisorLeft = criarDivisor(Divisor.HORIZONTAL_SPLIT, left, right, d);
		divisor.setLeftComponent(divisorLeft);
		setDividerLocation(divisorLeft);
	}

	public static void novoHorizontalRight(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor divisorRight = criarDivisor(Divisor.HORIZONTAL_SPLIT, left, right, d);
		divisor.setRightComponent(divisorRight);
		setDividerLocation(divisorRight);
	}

	public static void novoVerticalLeft(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor divisorLeft = criarDivisor(Divisor.VERTICAL_SPLIT, left, right, d);
		divisor.setLeftComponent(divisorLeft);
		setDividerLocation(divisorLeft);
	}

	public static void novoVerticalRight(Divisor divisor, Component left, Component right, Dimension d) {
		Divisor divisorRight = criarDivisor(Divisor.VERTICAL_SPLIT, left, right, d);
		divisor.setRightComponent(divisorRight);
		setDividerLocation(divisorRight);
	}

	private static Divisor criarDivisor(int orientacao, Component left, Component right, Dimension d) {
		Divisor divisor = new Divisor();

		if (orientacao == Divisor.HORIZONTAL_SPLIT) {
			right.setSize(d.width / 2, right.getHeight());
			left.setSize(d.width / 2, left.getHeight());
		} else {
			right.setSize(d);
			left.setSize(d);
		}

		divisor.setOrientation(orientacao);
		divisor.setRightComponent(right);
		divisor.setLeftComponent(left);
		divisor.setSize(d);

		return divisor;
	}

	public static void checarValido(Divisor divisor, Component comp) {
		Objects.requireNonNull(comp);

		if (divisor.getLeftComponent() != comp && divisor.getRightComponent() != comp) {
			throw new IllegalStateException();
		}
	}

	public static void setDividerLocation(Divisor divisor) {
		Component right = divisor.getRightComponent();
		Component left = divisor.getLeftComponent();

		if (divisor.getOrientation() == Divisor.HORIZONTAL_SPLIT) {
			divisor.setDividerLocation(divisor.getWidth() / 2);
		} else {
			divisor.setDividerLocation(divisor.getHeight() / 2);
		}

		if (left instanceof Fichario) {
			((Fichario) left).setDividerLocation(0);
		}

		if (right instanceof Fichario) {
			((Fichario) right).setDividerLocation(0);
		}

		SwingUtilities.updateComponentTreeUI(divisor);
	}

	public static void salvarLayout(XMLUtil xml, Divisor divisor) {
		if (xml == null || divisor == null) {
			return;
		}

		xml.abrirTag(Constantes.DIVISOR);
		xml.atributo(Constantes.ORIENTACAO, divisor.isHorizontal() ? Constantes.HORIZONTAL : Constantes.VERTICAL);
		xml.atributo(Constantes.LOCAL_DIV, divisor.getDividerLocation());
		xml.fecharTag();

		xml.abrirTag2(Constantes.LEFT);

		if (divisor.getLeftComponent() instanceof Divisor) {
			((Divisor) divisor.getLeftComponent()).salvarLayout(xml);
		} else if (divisor.getLeftComponent() instanceof Fichario) {
			((Fichario) divisor.getLeftComponent()).salvarLayout(xml);
		} else if (divisor.getLeftComponent() instanceof Controle) {
			((Controle) divisor.getLeftComponent()).salvarLayout(xml);
		}

		xml.finalizarTag(Constantes.LEFT);
		xml.abrirTag2(Constantes.RIGHT);

		if (divisor.getRightComponent() instanceof Divisor) {
			((Divisor) divisor.getRightComponent()).salvarLayout(xml);
		} else if (divisor.getRightComponent() instanceof Fichario) {
			((Fichario) divisor.getRightComponent()).salvarLayout(xml);
		} else if (divisor.getRightComponent() instanceof Controle) {
			((Controle) divisor.getRightComponent()).salvarLayout(xml);
		}

		xml.finalizarTag(Constantes.RIGHT);
		xml.finalizarTag(Constantes.DIVISOR);

	}

	public static void aplicarLayout(Obj obj, Divisor divisor) {
		if (obj == null || divisor == null) {
			return;
		}

		if (obj.isDivisor()) {
			Obj left = obj.getFilho(0);
			Obj right = obj.getFilho(1);

			if (divisor.getLeftComponent() instanceof Layout) {
				Layout leftLayout = (Layout) divisor.getLeftComponent();
				leftLayout.aplicarLayout(left);
			}

			if (divisor.getRightComponent() instanceof Layout) {
				Layout rightLayout = (Layout) divisor.getRightComponent();
				rightLayout.aplicarLayout(right);
			}

			String localDiv = obj.getValorAtributo(Constantes.LOCAL_DIV);
			divisor.setDividerLocation(Integer.parseInt(localDiv));
		} else {
			throw new IllegalStateException();
		}
	}

	public static void ajusteScroll(Divisor divisor) {
		if (divisor == null) {
			return;
		}

		if (divisor.getLeftComponent() instanceof Layout) {
			Layout leftLayout = (Layout) divisor.getLeftComponent();
			leftLayout.ajusteScroll();
		}

		if (divisor.getRightComponent() instanceof Layout) {
			Layout rightLayout = (Layout) divisor.getRightComponent();
			rightLayout.ajusteScroll();
		}
	}
}