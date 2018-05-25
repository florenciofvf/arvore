package br.com.arvore.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import br.com.arvore.mod.ModeloOrdenacao;
import br.com.arvore.rnd.CellRD;
import br.com.arvore.util.Util;

public class TabelaUtil {

	public static List<String> getValoresColuna(Tabela tabela, int coluna) {
		ModeloOrdenacao modelo = (ModeloOrdenacao) tabela.getModel();
		List<String> resposta = new ArrayList<>();
		int[] linhas = tabela.getSelectedRows();
		int total = modelo.getRowCount();

		for (int i = 0; i < total; i++) {
			Object obj = null;

			if (linhas == null || linhas.length == 0) {
				obj = modelo.getValueAt(i, coluna);

				if (obj != null && !Util.estaVazio(obj.toString())) {
					resposta.add(obj.toString());
				}
			} else {
				boolean contem = false;

				for (int j : linhas) {
					if (j == i) {
						contem = true;
					}
				}

				if (contem) {
					obj = modelo.getValueAt(i, coluna);

					if (obj != null && !Util.estaVazio(obj.toString())) {
						resposta.add(obj.toString());
					}
				}
			}
		}

		return resposta;
	}

	public static void configRenderer(Tabela tabela) {
		TableColumnModel columnModel = tabela.getColumnModel();

		if (columnModel.getColumnCount() > 0) {
			TableColumn tableColumn = columnModel.getColumn(0);
			tableColumn.setCellRenderer(new CellRD());
		}
	}
}