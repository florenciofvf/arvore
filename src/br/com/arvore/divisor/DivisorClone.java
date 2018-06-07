package br.com.arvore.divisor;

import java.awt.Component;

public interface DivisorClone {
	public void setDivisor(Divisor divisor);

	public void setLeft(boolean b);

	public Component clonar();
}