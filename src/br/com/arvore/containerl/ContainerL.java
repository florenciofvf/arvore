package br.com.arvore.containerl;

import java.awt.BorderLayout;
import java.awt.Component;

import br.com.arvore.comp.PanelBorder;
import br.com.arvore.container.Container;
import br.com.arvore.divisor.Divisor;
import br.com.arvore.divisor.DivisorListener;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.fichario.FicharioListener;

public class ContainerL extends PanelBorder {
	private static final long serialVersionUID = 1L;
	private final Listener listener = new Listener();

	@Override
	public void remove(Component comp) {
		super.remove(comp);

		if (comp instanceof Divisor) {
			Divisor divisor = (Divisor) comp;
			divisor.excluirOuvinte(listener);
		} else if (comp instanceof Fichario) {
			Fichario fichario = (Fichario) comp;
			fichario.excluirOuvinte(listener);
		} else {
			throw new IllegalStateException();
		}
	}

	public void set(Component comp) {
		add(BorderLayout.CENTER, comp);

		if (comp instanceof Divisor) {
			Divisor divisor = (Divisor) comp;
			divisor.excluirOuvinte(listener);
			divisor.adicionarOuvinte(listener);
		} else if (comp instanceof Fichario) {
			Fichario fichario = (Fichario) comp;
			fichario.excluirOuvinte(listener);
			fichario.adicionarOuvinte(listener);
		} else {
			throw new IllegalStateException();
		}
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

	private class Listener implements DivisorListener, FicharioListener {
		@Override
		public void excluidoLeft(Divisor divisor) {
		}

		@Override
		public void excluidoRight(Divisor divisor) {
		}

		@Override
		public void pedidoAtualizarObjeto(Container container) {
		}

		@Override
		public void pedidoDestacarObjeto(Container container) {
		}

		@Override
		public void containerSelecionado(Container container) {
		}

		@Override
		public void pedidoExcluirObjeto(Container container) {
		}

		@Override
		public void selecionadoObjeto(Container container) {
		}

		@Override
		public void containerExcluido(Container container) {
		}
	}
}