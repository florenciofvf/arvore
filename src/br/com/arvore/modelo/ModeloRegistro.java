package br.com.arvore.modelo;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import br.com.arvore.Objeto;
import br.com.arvore.banco.Persistencia;

public class ModeloRegistro implements TableModel {
	private final List<List<String>> REGISTROS;
	private final List<String> COLUNAS;

	public ModeloRegistro(List<String> colunas, List<List<String>> registros) {
		REGISTROS = registros;
		COLUNAS = colunas;
	}

	@Override
	public int getRowCount() {
		return REGISTROS.size();
	}

	@Override
	public int getColumnCount() {
		return COLUNAS.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return COLUNAS.get(columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final List<String> REGISTRO = REGISTROS.get(rowIndex);
		return REGISTRO.get(columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	public static ModeloRegistro criarModelo(Objeto objeto) throws Exception {
		return Persistencia.criarModeloRegistro(objeto);
	}
}