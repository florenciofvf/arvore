package br.com.arvore.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.com.arvore.Objeto;
import br.com.arvore.comp.TextArea;

public class Util {
	private static final boolean LOG_CONSOLE = false;

	private Util() {
	}

	public static void checarVazio(String s, String chave) {
		if (estaVazio(s)) {
			throw new IllegalArgumentException(Mensagens.getString(chave));
		}
	}

	public static boolean estaVazio(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static void mensagem(Component componente, String string) {
		TextArea textArea = new TextArea(string);
		textArea.setPreferredSize(new Dimension(500, 300));
		JOptionPane.showMessageDialog(componente, textArea, Mensagens.getString("label.atencao"),
				JOptionPane.PLAIN_MESSAGE);
	}

	public static String getStackTrace(String info, Exception ex) {
		StringWriter sw = new StringWriter();
		sw.append(info + "\r\n\r\n");

		if (ex != null) {
			if (LOG_CONSOLE) {
				ex.printStackTrace();
			} else {
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
			}
		}

		return sw.toString();
	}

	public static void ajustar(JTable table, Graphics graphics) {
		ajustar(table, graphics, Constantes.LARGURA_ICONE_ORDENAR);
	}

	public static void ajustar(JTable table, Graphics graphics, int ajuste) {
		DefaultTableColumnModel columnModel = (DefaultTableColumnModel) table.getColumnModel();
		FontMetrics fontMetrics = graphics.getFontMetrics();

		for (int col = 0; col < table.getColumnCount(); col++) {
			String coluna = table.getColumnName(col);
			int largura = fontMetrics.stringWidth(coluna);

			for (int lin = 0; lin < table.getRowCount(); lin++) {
				TableCellRenderer renderer = table.getCellRenderer(lin, col);

				Component component = renderer.getTableCellRendererComponent(table, table.getValueAt(lin, col), false,
						false, lin, col);

				largura = Math.max(largura, component.getPreferredSize().width);
			}

			TableColumn column = columnModel.getColumn(col);
			column.setPreferredWidth(largura + ajuste);
		}
	}

	public static String getSQL(Component componente, Objeto objeto) {
		TextArea textArea = new TextArea(objeto.getConsulta());
		textArea.setPreferredSize(new Dimension(500, 300));

		JOptionPane pane = new JOptionPane(Mensagens.getString("label.consulta"), JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_OPTION);
		pane.setMessage(textArea);

		JDialog dialog = pane.createDialog(componente, objeto.toString());
		dialog.setVisible(true);
		dialog.dispose();

		Object opcao = pane.getValue();

		if (opcao == null || !(opcao instanceof Integer)) {
			return null;
		}

		if (((Integer) opcao).intValue() != JOptionPane.YES_OPTION) {
			return null;
		}

		String consulta = textArea.getText();

		if (Util.estaVazio(consulta)) {
			return null;
		}

		return consulta;
	}
}