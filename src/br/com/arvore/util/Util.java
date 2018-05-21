package br.com.arvore.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.com.arvore.comp.TextArea;

public class Util {
	private static final boolean LOG_CONSOLE = false;

	private Util() {
	}

	public static void setActionESC(JDialog dialog) {
		JComponent component = (JComponent) dialog.getContentPane();

		InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "esc");

		Action action = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				WindowEvent event = new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING);
				EventQueue systemEventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
				systemEventQueue.postEvent(event);
			}
		};

		ActionMap actionMap = component.getActionMap();
		actionMap.put("esc", action);
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

	public static boolean confirmaExclusao(Component componente) {
		return JOptionPane.showConfirmDialog(componente, Mensagens.getString("msg.confirma_exclusao"),
				Mensagens.getString("label.atencao"), JOptionPane.YES_OPTION) == JOptionPane.OK_OPTION;
	}

	public static boolean confirmaAtualizacao(Component componente) {
		return JOptionPane.showConfirmDialog(componente, Mensagens.getString("msg.confirma_atualizacao"),
				Mensagens.getString("label.atencao"), JOptionPane.YES_OPTION) == JOptionPane.OK_OPTION;
	}

	public static void stackTraceMessageAndException(String tipo, Exception ex) throws Exception {
		String msg = getStackTrace(tipo, ex);
		mensagem(null, msg);
		throw new Exception();
	}

	public static void stackTraceAndMessage(String tipo, Exception ex, Component componente) {
		String msg = getStackTrace(tipo, ex);
		mensagem(componente, msg);
	}

	private static String getStackTrace(String info, Exception ex) {
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

	public static String normalizar(String s) {
		if (s == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		char anterior = '\0';

		for (char c : s.toCharArray()) {
			if (c == '\t') {
				if (anterior != '\t') {
					sb.append(" ");
				}
			} else {
				sb.append(c);
			}
			anterior = c;
		}

		return sb.toString();
	}

	public static String getStringLista(List<String> lista, boolean apostrofes) {
		StringBuilder sb = new StringBuilder();

		for (String string : lista) {
			if (estaVazio(string)) {
				continue;
			}

			if (sb.length() > 0) {
				sb.append(", ");
			}

			sb.append(apostrofes ? citar(string) : string);
		}

		return sb.toString();
	}

	private static String citar(String string) {
		return "'" + string + "'";
	}

	public static void setContentTransfered(String string) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		if (clipboard != null) {
			clipboard.setContents(new StringSelection(string), null);
		}
	}
}