package br.com.arvore.formulario;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import br.com.arvore.Objeto;
import br.com.arvore.compnte.Menu;
import br.com.arvore.compnte.MenuItem;
import br.com.arvore.compnte.RadioButtonMenuItem;
import br.com.arvore.compnte.SplitPane;
import br.com.arvore.compnte.SplitPaneListener;
import br.com.arvore.container.Container;
import br.com.arvore.controle.Controle;
import br.com.arvore.dialogo.DialogoConexao;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.fichario.FicharioListener;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private final RadioButtonMenuItem radioItemHorizontal = new RadioButtonMenuItem("label.horizontal");
	private final RadioButtonMenuItem radioItemVertical = new RadioButtonMenuItem("label.vertical");
	private final RadioButtonMenuItem radioItemNormal = new RadioButtonMenuItem("label.normal");
	private final MenuItem itemConexao = new MenuItem("label.conexao", Icones.BANCO);
	private final MenuItem itemFechar = new MenuItem("label.fechar", Icones.SAIR);
	private final MenuItem itemAbrir = new MenuItem("label.abrir", Icones.ABRIR);
	private final SplitPane splitPane = new SplitPane(SplitPane.VERTICAL_SPLIT);
	private final Menu menuAparencia = new Menu("label.aparencia");
	private final Menu menuArquivo = new Menu("label.arquivo");
	private final SplitPane splitPaneLayout = new SplitPane();
	private final Menu menuLayout = new Menu("label.layout");
	private final JMenuBar menuBar = new JMenuBar();
	private Fichario ficharioEspelho;
	private final Fichario fichario;
	private final Controle controle;
	private byte organizacao;

	public Formulario() {
		setTitle(Mensagens.getString("label.arvore"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		controle = new Controle(this);
		fichario = new Fichario(this);
		fichario.adicionarOuvinte(ficharioListener);
		setSize(1000, 700);
		montarLayout();
		montarMenu();
		configurar();
	}

	private void criarFicharioEspelho() {
		Objeto raiz = fichario.getRaiz();

		if (raiz == null) {
			return;
		}

		try {
			ficharioEspelho = new Fichario(this);
			ficharioEspelho.adicionarOuvinte(ficharioListener);
			ficharioEspelho.addAba("label.objetos", raiz, true);
			ficharioEspelho.setRaiz(raiz);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("CRIAR ESPELHO", ex, this);
		}
	}

	private void configurar() {
		itemFechar.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
		itemConexao.addActionListener(e -> new DialogoConexao(this));
		FormularioUtil.configMAC(this);

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				fichario.abrirArquivo(new File(Constantes.NOME_ARQUIVO_PADRAO), false, false, false);
				splitPane.setDividerLocation(0.78);
				controle.selecionadoObjeto(null);
			};

			public void windowClosing(WindowEvent e) {
				FormularioUtil.fechar(Formulario.this);
			};
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
		splitPane.setListener(splitPaneListener);
		add(BorderLayout.CENTER, splitPane);
	}

	private void montarMenu() {
		setJMenuBar(menuBar);

		menuBar.add(menuArquivo);
		menuBar.add(menuAparencia);
		menuBar.add(menuLayout);

		menuArquivo.add(itemAbrir);
		menuArquivo.addSeparator();
		menuArquivo.add(itemConexao);
		menuArquivo.addSeparator();
		menuArquivo.add(itemFechar);

		configMenuAparencia();

		menuLayout.add(radioItemNormal);
		menuLayout.addSeparator();
		menuLayout.add(radioItemVertical);
		menuLayout.addSeparator();
		menuLayout.add(radioItemHorizontal);

		ButtonGroup grupo = new ButtonGroup();
		grupo.add(radioItemNormal);
		grupo.add(radioItemVertical);
		grupo.add(radioItemHorizontal);

		radioItemNormal.addActionListener(layoutListener);
		radioItemVertical.addActionListener(layoutListener);
		radioItemHorizontal.addActionListener(layoutListener);

		itemAbrir.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));
	}

	public void organizacaoNormal() {
		organizacao = Constantes.ORGANIZACAO_NORMAL;
		splitPane.setLeftComponent(fichario);
		radioItemNormal.setSelected(true);
		ficharioEspelho = null;
	}

	private void configMenuAparencia() {
		LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
		ButtonGroup grupo = new ButtonGroup();

		for (LookAndFeelInfo info : installedLookAndFeels) {
			ItemAparencia item = new ItemAparencia(this, info);
			menuAparencia.add(item);
			grupo.add(item);
		}
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
			controle.selecionadoObjeto(container);
			controle.arvore();
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

	private ActionListener layoutListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (radioItemHorizontal.isSelected() || radioItemVertical.isSelected()) {
				if (ficharioEspelho == null) {
					criarFicharioEspelho();
				}

				if (ficharioEspelho == null) {
					return;
				}

				if (organizacao == Constantes.ORGANIZACAO_NORMAL) {
					splitPaneLayout.setLeftComponent(fichario);
					splitPaneLayout.setRightComponent(ficharioEspelho);
					splitPane.setLeftComponent(splitPaneLayout);
				}
			}

			if (radioItemNormal.isSelected()) {
				if (organizacao == Constantes.ORGANIZACAO_HORIZONTAL
						|| organizacao == Constantes.ORGANIZACAO_VERTICAL) {
					organizacao = Constantes.ORGANIZACAO_NORMAL;
					splitPane.setLeftComponent(fichario);
				}
			}

			if (radioItemHorizontal.isSelected()) {
				organizacao = Constantes.ORGANIZACAO_HORIZONTAL;
				splitPaneLayout.setOrientation(SplitPane.HORIZONTAL_SPLIT);
				splitPaneLayout.setDividerLocation(getWidth() / 2);
			}

			if (radioItemVertical.isSelected()) {
				organizacao = Constantes.ORGANIZACAO_VERTICAL;
				splitPaneLayout.setOrientation(SplitPane.VERTICAL_SPLIT);
				splitPaneLayout.setDividerLocation(getHeight() / 2);
			}

			splitPane.setDividerLocation(Constantes.DIV_FICHARIO_CONTROLE);
		}
	};

	private SplitPaneListener splitPaneListener = new SplitPaneListener() {
		@Override
		public void localizacao(int i) {
			Constantes.DIV_FICHARIO_CONTROLE = i;
		}
	};
}