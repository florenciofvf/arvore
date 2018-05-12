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
import br.com.arvore.util.Constantes;
import br.com.arvore.util.OrdemCellRD;

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
	}

	private class Listener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= Constantes.DOIS && tableListener != null) {
				int tableColuna = columnAtPoint(e.getPoint());
				int modelColuna = convertColumnIndexToModel(tableColuna);

				descendente = !descendente;

				TableColumnModel columnModel = getColumnModel();
				TableColumn tableColumn = columnModel.getColumn(tableColuna);
				TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();

				if (!(headerRenderer instanceof OrdemCellRD)) {
					int largura = tableColumn.getPreferredWidth() + Constantes.LARGURA_ICONE_ORDENAR;
					tableColumn.setPreferredWidth(largura);
				}

				tableListener.ordenarColuna(tableColumn, descendente, modelColuna);
			}
		}
	}
}