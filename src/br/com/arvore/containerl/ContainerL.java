package br.com.arvore.containerl;

import java.awt.BorderLayout;
import java.awt.Component;

import br.com.arvore.comp.PanelBorder;
import br.com.arvore.fichario.Fichario;

public class ContainerL extends PanelBorder {
	private static final long serialVersionUID = 1L;

	public ContainerL() {
	}

	@Override
	public void add(Component comp, Object constraints) {
		super.add(comp, constraints);

		// if (comp instanceof Divisor) {
		// Divisor divisor = (Divisor) comp;
		// divisor.adicionarOuvinte(listener);
		// } else if (comp instanceof Divisor) {
		// Fichario fichario = (Fichario) comp;
		// fichario.adicionarOuvinte(listener);
		// } else {
		// throw new IllegalStateException();
		// }
	}

	@Override
	public void remove(Component comp) {
		super.remove(comp);

		// if (comp instanceof Divisor) {
		// Divisor divisor = (Divisor) comp;
		// divisor.excluirOuvinte(listener);
		// } else if (comp instanceof Divisor) {
		// Fichario fichario = (Fichario) comp;
		// fichario.excluirOuvinte(listener);
		// } else {
		// throw new IllegalStateException();
		// }
	}

	public void set(Component comp) {
		add(BorderLayout.CENTER, comp);
	}

	public void setDividerLocation() {
		if (getComponentCount() > 0) {
			Component comp = getComponent(0);

			if (comp instanceof Fichario) {
				Fichario fichario = (Fichario) comp;
				fichario.setDividerLocation(0);
			}
		}
	}
}