package br.com.arvore.tabela;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.Popup;
import br.com.arvore.mod.ModeloOrdenacao;
import br.com.arvore.rnd.HeaderRD;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Util;

public class Tabela extends JTable {
	private static final long serialVersionUID = 1L;
	private PopupHeader popupHeader = new PopupHeader();
	private final List<TabelaListener> ouvintes;
	private boolean descendente;

	public Tabela() {
		this(new ModeloOrdenacao());
	}

	public Tabela(ModeloOrdenacao dm) {
		super(dm);
		ouvintes = new ArrayList<>();
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableHeader.addMouseListener(headerListener);

		popupHeader.itemCopiar.addActionListener(e -> {
			List<String> resp = TabelaUtil.getValoresColuna(Tabela.this, popupHeader.getTag());
			Util.setContentTransfered(Util.getStringLista(resp, false));
		});

		popupHeader.itemCopiarComAspas.addActionListener(e -> {
			List<String> resp = TabelaUtil.getValoresColuna(Tabela.this, popupHeader.getTag());
			Util.setContentTransfered(Util.getStringLista(resp, true));
		});
	}

	public void adicionarOuvinte(TabelaListener listener) {
		ouvintes.add(listener);
	}

	public void limparOuvintes() {
		ouvintes.clear();
	}

	private void notificarOrdenacaoColuna(TableColumn tableColumn, int modelColuna) {
		for (TabelaListener listener : ouvintes) {
			listener.ordenarColuna(tableColumn, descendente, modelColuna);
		}
	}

	@Override
	public void setModel(TableModel dataModel) {
		if (!(dataModel instanceof ModeloOrdenacao)) {
			throw new IllegalStateException();
		}

		super.setModel(dataModel);
		TabelaUtil.configRenderer(this);
	}

	private MouseListener headerListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			processar(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			processar(e);
		}

		private void processar(MouseEvent e) {
			if (!e.isPopupTrigger()) {
				return;
			}

			int tableColuna = columnAtPoint(e.getPoint());
			int modelColuna = convertColumnIndexToModel(tableColuna);
			popupHeader.setTag(modelColuna);
			popupHeader.show(tableHeader, e.getX(), e.getY());
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= Constantes.DOIS) {
				int tableColuna = columnAtPoint(e.getPoint());
				int modelColuna = convertColumnIndexToModel(tableColuna);

				if (modelColuna == 0) {
					return;
				}

				descendente = !descendente;

				TableColumnModel columnModel = getColumnModel();
				TableColumn tableColumn = columnModel.getColumn(tableColuna);
				TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();

				if (!(headerRenderer instanceof HeaderRD)) {
					int largura = tableColumn.getPreferredWidth() + Constantes.LARGURA_ICONE_ORDENAR;
					tableColumn.setPreferredWidth(largura);
				}

				notificarOrdenacaoColuna(tableColumn, modelColuna);
			}
		}
	};

	private class PopupHeader extends Popup {
		private static final long serialVersionUID = 1L;
		final MenuItem itemCopiarComAspas = new MenuItem("label.copiar_com_aspas");
		final MenuItem itemCopiar = new MenuItem("label.copiar");
		private int tag;

		public PopupHeader() {
			add(itemCopiar);
			addSeparator();
			add(itemCopiarComAspas);
		}

		public int getTag() {
			return tag;
		}

		public void setTag(int tag) {
			this.tag = tag;
		}
	}
}