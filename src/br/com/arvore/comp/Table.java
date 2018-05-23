package br.com.arvore.comp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import br.com.arvore.modelo.ModeloOrdenacao;
import br.com.arvore.rd.CellRD;
import br.com.arvore.rd.HeaderRD;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Util;

public class Table extends JTable {
	private static final long serialVersionUID = 1L;
	private TableListener tableListener;
	private Popup popup = new Popup();
	private boolean descendente;

	public Table() {
		this(new ModeloOrdenacao());
	}

	public Table(ModeloOrdenacao dm) {
		super(dm);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableHeader.addMouseListener(new Listener());

		popup.itemCopiar.addActionListener(e -> {
			List<String> resp = getValores(popup.getTag());
			Util.setContentTransfered(Util.getStringLista(resp, false));
		});

		popup.itemCopiarComAspas.addActionListener(e -> {
			List<String> resp = getValores(popup.getTag());
			Util.setContentTransfered(Util.getStringLista(resp, true));
		});
	}

	@Override
	public void setModel(TableModel dataModel) {
		super.setModel(dataModel);

		if (dataModel instanceof TableListener) {
			tableListener = (TableListener) dataModel;
		}

		configurar(this);
	}

	private void configurar(Table table) {
		TableColumnModel columnModel = table.getColumnModel();

		if (columnModel.getColumnCount() > 0) {
			TableColumn column = columnModel.getColumn(0);
			column.setCellRenderer(new CellRD());
		}
	}

	private class Listener extends MouseAdapter {
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
			popup.setTag(modelColuna);
			popup.show(tableHeader, e.getX(), e.getY());
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= Constantes.DOIS && tableListener != null) {
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

				tableListener.ordenarColuna(tableColumn, descendente, modelColuna);
			}
		}
	}

	public List<String> getValores(int coluna) {
		List<String> resposta = new ArrayList<>();
		int[] is = getSelectedRows();

		ModeloOrdenacao atual = (ModeloOrdenacao) getModel();
		int total = atual.getRowCount();

		for (int i = 0; i < total; i++) {
			Object obj = null;

			if (is == null || is.length == 0) {
				obj = atual.getValueAt(i, coluna);

				if (obj != null && !Util.estaVazio(obj.toString())) {
					resposta.add(obj.toString());
				}
			} else {
				boolean contem = false;

				for (int j : is) {
					if (j == i) {
						contem = true;
					}
				}

				if (contem) {
					obj = atual.getValueAt(i, coluna);

					if (obj != null && !Util.estaVazio(obj.toString())) {
						resposta.add(obj.toString());
					}
				}
			}
		}

		return resposta;
	}
}

class Popup extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	final MenuItem itemCopiarComAspas = new MenuItem("label.copiar_com_aspas");
	final MenuItem itemCopiar = new MenuItem("label.copiar");
	private int tag;

	public Popup() {
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