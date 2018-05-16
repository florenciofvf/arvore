package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import br.com.arvore.banco.Conexao;
import br.com.arvore.comp.Menu;
import br.com.arvore.comp.MenuItem;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private final MenuItem itemConexao = new MenuItem("label.conexao", Icones.BANCO);
	private final MenuItem itemFechar = new MenuItem("label.fechar", Icones.SAIR);
	private final MenuItem itemAbrir = new MenuItem("label.abrir", Icones.ABRIR);
	private final Menu menuAparencia = new Menu("label.aparencia");
	private final Menu menuArquivo = new Menu("label.arquivo");
	private final JMenuBar menuBar = new JMenuBar();
	private final Fichario fichario;

	public Formulario() {
		setTitle(Mensagens.getString("label.arvore"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		fichario = new Fichario(this);
		setSize(700, 700);
		montarLayout();
		configurar();
	}

	private void configurar() {
		if (System.getProperty("os.name").startsWith("Mac OS")) {
			try {
				Class<?> classe = Class.forName("com.apple.eawt.FullScreenUtilities");
				Method method = classe.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE);
				method.invoke(classe, this, true);
			} catch (Exception ex) {
				Util.stackTraceAndMessage(getClass().getName() + ".setWindowCanFullScreen()", ex, this);
			}
		}

		itemConexao.addActionListener(e -> new ConexaoDialogo(this));

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				fichario.abrirArquivo(new File("modelo.xml"), false, false, false);
			};

			public void windowClosing(WindowEvent e) {
				try {
					Conexao.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			};
		});

		itemFechar.addActionListener(e -> {
			try {
				Conexao.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		});

		itemAbrir.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.showOpenDialog(Formulario.this);

			File file = fileChooser.getSelectedFile();
			if (file != null) {
				fichario.abrirArquivo(file, true, true, true);
			}
		});
	}

	private void montarLayout() {
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, fichario);

		menuArquivo.add(itemAbrir);
		menuArquivo.addSeparator();
		menuArquivo.add(itemConexao);
		menuArquivo.addSeparator();
		menuArquivo.add(itemFechar);
		menuBar.add(menuArquivo);
		menuBar.add(menuAparencia);
		configMenuAparencia();
		setJMenuBar(menuBar);
	}

	private void configMenuAparencia() {
		LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
		ButtonGroup grupo = new ButtonGroup();

		for (LookAndFeelInfo lookAndFeelInfo : installedLookAndFeels) {
			ItemMenu itemMenu = new ItemMenu(lookAndFeelInfo);
			menuAparencia.add(itemMenu);
			grupo.add(itemMenu);
		}
	}

	private class ItemMenu extends JRadioButtonMenuItem {
		private static final long serialVersionUID = 1L;
		private final String classe;

		public ItemMenu(LookAndFeelInfo info) {
			super(info.getName());
			classe = info.getClassName();

			addActionListener(e -> {
				try {
					UIManager.setLookAndFeel(classe);
					SwingUtilities.updateComponentTreeUI(Formulario.this);
				} catch (Exception ex) {
					Util.stackTraceAndMessage(getClass().getName() + ".ItemMenu()", ex, Formulario.this);
				}
			});
		}
	}
}