package br.com.arvore.comp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import br.com.arvore.modelo.ModeloOrdenacao;
import br.com.arvore.util.CellRD;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.HeaderRD;

public class Table extends JTable {
	private static final long serialVersionUID = 1L;
	private TableListener tableListener;
	private boolean descendente;

	public Table() {
		this(new ModeloOrdenacao());
	}

	public Table(ModeloOrdenacao dm) {
		super(dm);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JTableHeader tableHeader = getTableHeader();
		tableHeader.addMouseListener(new Listener());
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
}