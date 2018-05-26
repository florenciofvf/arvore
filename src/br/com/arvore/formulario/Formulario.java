package br.com.arvore.formulario;

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
import br.com.arvore.componente.Menu;
import br.com.arvore.componente.MenuItem;
import br.com.arvore.componente.SplitPane;
import br.com.arvore.container.Container;
import br.com.arvore.dialogo.DialogoConexao;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.fichario.FicharioListener;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private final MenuItem itemConexao = new MenuItem("label.conexao", Icones.BANCO);
	private final MenuItem itemFechar = new MenuItem("label.fechar", Icones.SAIR);
	private final MenuItem itemAbrir = new MenuItem("label.abrir", Icones.ABRIR);
	private final SplitPane splitPane = new SplitPane(SplitPane.VERTICAL_SPLIT);
	private final Menu menuAparencia = new Menu("label.aparencia");
	private final Menu menuArquivo = new Menu("label.arquivo");
	private final JMenuBar menuBar = new JMenuBar();
	private final Controle controle;
	private final Fichario fichario;

	public Formulario() {
		setTitle(Mensagens.getString("label.arvore"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		controle = new Controle(this);
		fichario = new Fichario(this);
		fichario.adicionarOuvinte(ficharioListener);
		setSize(1000, 700);
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

		itemConexao.addActionListener(e -> new DialogoConexao(this));

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				fichario.abrirArquivo(new File(Constantes.NOME_ARQUIVO_PADRAO), false, false, false);
				splitPane.setDividerLocation(Constantes.DIV_FICHARIO_CONTROLE);
				controle.selecionadoObjeto(null);
			};

			public void windowClosing(WindowEvent e) {
				try {
					Conexao.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			};
		});

		itemFechar.addActionListener(e -> {
			try {
				Conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			System.exit(0);
		});

		itemAbrir.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser(".");
			int opcao = fileChooser.showOpenDialog(Formulario.this);

			if (opcao != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File file = fileChooser.getSelectedFile();

			if (file != null) {
				fichario.abrirArquivo(file, true, true, true);
			}
		});
	}

	private void montarLayout() {
		setLayout(new BorderLayout());

		splitPane.setLeftComponent(fichario);
		splitPane.setRightComponent(controle);
		add(BorderLayout.CENTER, splitPane);

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

	private FicharioListener ficharioListener = new FicharioListener() {
		@Override
		public void selecionadoObjeto(Container container) {
			controle.selecionadoObjeto(container);
		}

		@Override
		public void pedidoExcluirObjeto(Container container) {
			controle.acaoObjeto(container, "label.delete");
		}

		@Override
		public void pedidoDestacarObjeto(Container container) {
			controle.destacarObjeto(container);
		}

		@Override
		public void pedidoAtualizarObjeto(Container container) {
			controle.acaoObjeto(container, "label.arvore");
		}

		@Override
		public void containerSelecionado(Container container) {
			controle.selecionadoObjeto(container);
		}

		@Override
		public void containerExcluido(Container container) {
			controle.containerExcluido(container);
		}
	};

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