package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.ArvoreListener;
import br.com.arvore.comp.PanelBorder;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.comp.SplitPane;
import br.com.arvore.comp.Table;
import br.com.arvore.modelo.ModeloOrdenacao;
import br.com.arvore.modelo.ModeloRegistro;
import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Util;

public class PainelAba extends PanelBorder implements ArvoreListener {
	private static final long serialVersionUID = 1L;
	private final SplitPane split = new SplitPane();
	private final Table table = new Table();
	private final Popup popup = new Popup();
	private final Arvore arvore;
	private Objeto selecionado;

	public PainelAba(Arvore arvore) {
		arvore.setArvoreListener(this);
		this.arvore = arvore;
		montarLayout();
		configurar();
	}

	private void montarLayout() {
		add(BorderLayout.CENTER, split);
		split.setLeftComponent(new ScrollPane(arvore));
		split.setRightComponent(new ScrollPane(table));
		split.setDividerLocation(Constantes.LAR_SPLIT);
	}

	private void configurar() {
		popup.itemAtualizar.addActionListener(e -> inflar(selecionado, "ATUALIZAR"));

		popup.itemRegistros.addActionListener(e -> criarModeloRegistro(selecionado));

		popup.itemFiltro.addActionListener(e -> {
			String consulta = Util.getSQL(this, selecionado);

			if (consulta == null) {
				return;
			}

			selecionado.setConsulta(consulta);
			inflar(selecionado, "FILTRO");
		});
	}

	private void inflar(Objeto objeto, String tipo) {
		try {
			if (Constantes.INFLAR_ANTECIPADO) {
				objeto.inflar();
			} else {
				objeto.inflarParcial2();
			}
			ArvoreUtil.atualizar(arvore, objeto);
		} catch (Exception ex) {
			String msg = Util.getStackTrace(tipo, ex);
			Util.mensagem(this, msg);
		}
	}

	private void criarModeloRegistro(Objeto objeto) {
		try {
			ModeloRegistro modeloRegistro = ModeloRegistro.criarModelo(objeto);
			ModeloOrdenacao modeloOrdenacao = new ModeloOrdenacao(modeloRegistro, modeloRegistro.getColunasNumero());

			table.setModel(modeloOrdenacao);
			Util.ajustar(table, getGraphics());
		} catch (Exception ex) {
			String msg = Util.getStackTrace("REGISTROS", ex);
			Util.mensagem(this, msg);
		}
	}

	@Override
	public void exibirPopup(Arvore arvore, Objeto selecionado, MouseEvent e) {
		popup.setHabilitarRegistros(!Util.estaVazio(selecionado.getPesquisa()));
		popup.setHabilitarFiltro(!Util.estaVazio(selecionado.getConsulta()));
		this.selecionado = selecionado;
		popup.show(arvore, e.getX(), e.getY());
	}

	@Override
	public void clicado(Objeto objeto) {
		if (objeto.isPesquisaPopup() || Util.estaVazio(objeto.getPesquisa())) {
			return;
		}

		criarModeloRegistro(objeto);
	}
}