package br.com.arvore.mod;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import br.com.arvore.comp.TableListener;
import br.com.arvore.rnd.HeaderRD;
import br.com.arvore.util.Util;

public class ModeloOrdenacao extends AbstractTableModel implements TableListener {
	private static final long serialVersionUID = 1L;
	private final boolean[] colunasNumero;
	private final TableModel model;
	private boolean ordenarNumero;
	private final Linha[] linhas;
	private int colunaOrdenacao;
	private boolean descendente;

	public ModeloOrdenacao() {
		this(new ModeloVazio(), new boolean[] {});
	}

	public ModeloOrdenacao(TableModel model, boolean[] colunasNumero) {
		this.linhas = new Linha[model.getRowCount()];
		this.colunasNumero = colunasNumero;
		this.model = model;

		for (int i = 0; i < linhas.length; i++) {
			linhas[i] = new Linha(i);
		}
	}

	private void ordenar(int coluna) {
		colunaOrdenacao = coluna;
		Arrays.sort(linhas);

		if (model instanceof ModeloRegistro) {
			int qtdLinhas = getRowCount();

			for (int rowIndex = 0; rowIndex < qtdLinhas; rowIndex++) {
				((ModeloRegistro) model).configNumLinha(linhas[rowIndex].indice, rowIndex + 1);
			}
		}

		fireTableDataChanged();
	}

	@Override
	public void ordenarColuna(TableColumn tableColumn, boolean descendente, int modelColuna) {
		if (modelColuna > 0) {
			ordenarNumero = colunasNumero[modelColuna];
			tableColumn.setHeaderRenderer(new HeaderRD(descendente, ordenarNumero));
			this.descendente = descendente;
			ordenar(modelColuna);
		}
	}

	@Override
	public int getRowCount() {
		return model.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return model.getColumnCount();
	}

	@Override
	public String getColumnName(int column) {
		return model.getColumnName(column);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return model.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return model.getValueAt(linhas[rowIndex].indice, columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return model.isCellEditable(linhas[rowIndex].indice, columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		model.setValueAt(aValue, linhas[rowIndex].indice, columnIndex);
	}

	private class Linha implements Comparable<Linha> {
		private final int indice;

		public Linha(int indice) {
			this.indice = indice;
		}

		@Override
		public int compareTo(Linha o) {
			String string = (String) model.getValueAt(indice, colunaOrdenacao);
			String outra = (String) model.getValueAt(o.indice, colunaOrdenacao);

			if (ordenarNumero) {
				Long valor = Util.estaVazio(string) ? 0 : Long.valueOf(string);
				Long outro = Util.estaVazio(outra) ? 0 : Long.valueOf(outra);

				if (descendente) {
					return valor.compareTo(outro);
				} else {
					return outro.compareTo(valor);
				}
			} else {
				string = Util.estaVazio(string) ? "" : string;
				outra = Util.estaVazio(outra) ? "" : outra;

				if (descendente) {
					return string.compareTo(outra);
				} else {
					return outra.compareTo(string);
				}
			}
		}
	}
}