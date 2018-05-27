package br.com.arvore.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import br.com.arvore.Objeto;
import br.com.arvore.banco.Persistencia;

public class ModeloRegistro implements TableModel {
	private final List<List<String>> REGISTROS;
	private final List<String> COLUNAS;
	private boolean[] colunasNumero;

	public ModeloRegistro(List<String> colunas, List<List<String>> registros) {
		REGISTROS = registros;
		COLUNAS = colunas;
	}

	public static ModeloRegistro criarModeloRegistroVazio() {
		ModeloRegistro modelo = new ModeloRegistro(new ArrayList<>(), new ArrayList<>());
		modelo.colunasNumero = new boolean[0];

		return modelo;
	}

	public boolean[] getColunasNumero() {
		return colunasNumero;
	}

	public void setColunasNumero(boolean[] colunasNumero) {
		this.colunasNumero = colunasNumero;
	}

	void configNumLinha(int linha, int valor) {
		if (linha < REGISTROS.size()) {
			List<String> registro = REGISTROS.get(linha);

			if (registro.size() > 0) {
				registro.set(0, "" + valor);
			}
		}
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