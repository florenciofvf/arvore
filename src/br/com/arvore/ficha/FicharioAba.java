package br.com.arvore.ficha;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.ArvoreListener;
import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.PanelBorder;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.comp.SplitPane;
import br.com.arvore.comp.SplitPaneListener;
import br.com.arvore.dialog.DialogoObjeto;
import br.com.arvore.mod.ModeloOrdenacao;
import br.com.arvore.mod.ModeloRegistro;
import br.com.arvore.tabela.Tabela;
import br.com.arvore.tabela.TabelaUtil;
import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Util;

public class FicharioAba extends PanelBorder {
	private static final long serialVersionUID = 1L;
	private final SplitPane splitPane = new SplitPane();
	private final Tabela tabela = new Tabela();
	private final Popup popup = new Popup();
	private final Arvore arvore;
	private Objeto selecionado;

	public FicharioAba(Arvore arvore) {
		arvore.adicionarOuvinte(new ListenerArvore());
		this.arvore = arvore;
		montarLayout();
		configurar();
	}

	public Arvore getArvore() {
		return arvore;
	}

	private void montarLayout() {
		add(BorderLayout.CENTER, splitPane);
		splitPane.setListener(new ListenerSplitPane());
		splitPane.setLeftComponent(new ScrollPane(arvore));
		splitPane.setRightComponent(new ScrollPane(tabela));
		splitPane.setDividerLocation(Constantes.DIV_ARVORE_TABELA);
	}

	private void configurar() {
		popup.itemAtualizar.addActionListener(e -> atualizarArvore(selecionado));
		popup.itemDelete.addActionListener(e -> pedidoExclusao(selecionado));
		popup.itemDestacar
				.addActionListener(e -> new DialogoObjeto(SwingUtilities.getWindowAncestor(this), selecionado));
	}

	void criarModeloRegistro(Objeto objeto) {
		try {
			ModeloRegistro modeloRegistro = ModeloRegistro.criarModelo(objeto);
			ModeloOrdenacao modeloOrdenacao = new ModeloOrdenacao(modeloRegistro, modeloRegistro.getColunasNumero());
			tabela.limparOuvintes();
			tabela.adicionarOuvinte(modeloOrdenacao);
			tabela.setModel(modeloOrdenacao);
			TabelaUtil.ajustar(tabela, getGraphics());
		} catch (Exception ex) {
			Util.stackTraceAndMessage("EXIBIR REGISTROS", ex, this);
		}
	}

	void atualizarArvore(Objeto objeto) {
		try {
			ArvoreUtil.inflar(objeto);
			ArvoreUtil.atualizarEstrutura(arvore, objeto);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("ATUALIZAR ARVORE", ex, this);
		}
	}

	void excluirArvore(Objeto objeto) {
		ArvoreUtil.excluirEstrutura(arvore, objeto);
	}

	void pedidoExclusao(Objeto objeto) {
		arvore.excluir(objeto);
	}

	private class ListenerArvore implements ArvoreListener {
		@Override
		public void exibirPopup(Arvore arvore, Objeto selecionado, MouseEvent e) {
			popup.itemDelete.setEnabled(!Util.estaVazio(selecionado.getInstrucaoDelete()));
			FicharioAba.this.selecionado = selecionado;
			popup.show(arvore, e.getX(), e.getY());
		}

		@Override
		public void pedidoExclusao(Arvore arvore, Objeto objeto) {
		}

		@Override
		public void clicado(Arvore arvore, Objeto objeto) {
			if (Constantes.INFLAR_DESATIVADO) {
				return;
			}

			if (objeto.isPesquisaPopup() || Util.estaVazio(objeto.getInstrucaoTabela())) {
				tabela.setModel(new ModeloOrdenacao());
				return;
			}

			criarModeloRegistro(objeto);
		}
	}

	private class ListenerSplitPane implements SplitPaneListener {
		@Override
		public void localizacao(int i) {
			Constantes.DIV_ARVORE_TABELA = i;
		}
	}
}

class Popup extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	final MenuItem itemAtualizar = new MenuItem("label.atualizar", Icones.ATUALIZAR);
	final MenuItem itemDestacar = new MenuItem("label.destacar", Icones.DESCONECTA);
	final MenuItem itemDelete = new MenuItem("label.delete", Icones.EXCLUIR);

	public Popup() {
		add(itemAtualizar);
		addSeparator();
		add(itemDestacar);
		addSeparator();
		add(itemDelete);
	}
}