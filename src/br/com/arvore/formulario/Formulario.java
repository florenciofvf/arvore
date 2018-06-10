package br.com.arvore.formulario;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Menu;
import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.Panel;
import br.com.arvore.comp.SplitPaneListener;
import br.com.arvore.container.Container;
import br.com.arvore.controle.Controle;
import br.com.arvore.dialogo.DialogoConexao;
import br.com.arvore.divisor.Divisor;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.fichario.FicharioListener;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Obj;
import br.com.arvore.util.Util;
import br.com.arvore.util.XMLUtil;
import br.com.arvore.xml.XML;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private final MenuItem itemConexao = new MenuItem("label.conexao", Icones.BANCO);
	private final MenuItem itemAplicarModelo = new MenuItem("label.aplicar_modelo");
	private final MenuItem itemFechar = new MenuItem("label.fechar", Icones.SAIR);
	private final MenuItem itemSalvarModelo = new MenuItem("label.salvar_modelo");
	private final MenuItem itemAbrir = new MenuItem("label.abrir", Icones.ABRIR);
	private final Menu menuAparencia = new Menu("label.aparencia");
	public static final Pnl_padrao PNL_PADRAO = new Pnl_padrao();
	private final Menu menuArquivo = new Menu("label.arquivo");
	private final JMenuBar menuBar = new JMenuBar();
	private final Divisor divisor = new Divisor();
	private final Controle controle;
	private Objeto raiz;

	public Formulario() {
		setTitle(Mensagens.getString("label.arvore"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		controle = new Controle(this);
		setLayout(new BorderLayout());
		Constantes.DIV_CONTROLE = 500;
		setSize(1000, 700);
		montarLayout();
		montarMenu();
		configurar();
	}

	private void configurar() {
		itemFechar.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
		itemConexao.addActionListener(e -> new DialogoConexao(this));
		FormularioUtil.configMAC(this);

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				abrirArquivo(new File(Constantes.NOME_ARQUIVO_PADRAO), false, false, false);
				divisor.setDividerLocation(Constantes.DIV_CONTROLE);
				divisor.setListener(splitPaneListener);
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
				abrirArquivo(file, true, true, true);
			}
		});

		itemAplicarModelo.addActionListener(e -> {
			if (raiz == null) {
				Util.mensagem(this, Mensagens.getString("erro.sem_raiz_para_modelo"));
				return;
			}

			JFileChooser fileChooser = new JFileChooser(".");
			int opcao = fileChooser.showOpenDialog(Formulario.this);

			if (opcao != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File file = fileChooser.getSelectedFile();

			if (file != null) {
				aplicarModelo(file);
			}
		});

		itemSalvarModelo.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser(".");
			int opcao = fileChooser.showSaveDialog(Formulario.this);

			if (opcao != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File file = fileChooser.getSelectedFile();

			if (file != null) {
				salvarModelo(file);
			}
		});
	}

	private void montarLayout() {
		divisor.setOrientation(Divisor.VERTICAL_SPLIT);
		divisor.setLeftComponent(PNL_PADRAO);
		divisor.setRightComponent(controle);
		add(BorderLayout.CENTER, divisor);
	}

	private void montarMenu() {
		setJMenuBar(menuBar);

		menuBar.add(menuArquivo);
		menuBar.add(menuAparencia);

		menuArquivo.add(itemAbrir);
		menuArquivo.addSeparator();
		menuArquivo.add(itemConexao);
		menuArquivo.addSeparator();
		menuArquivo.add(itemAplicarModelo);
		menuArquivo.add(itemSalvarModelo);
		menuArquivo.addSeparator();
		menuArquivo.add(itemFechar);

		menuAparencia();

		itemAbrir.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));
	}

	private void menuAparencia() {
		LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
		ButtonGroup grupo = new ButtonGroup();

		for (LookAndFeelInfo info : installedLookAndFeels) {
			ItemAparencia item = new ItemAparencia(this, info);
			menuAparencia.add(item);
			grupo.add(item);
		}
	}

	private void salvarModelo(File file) {
		try {
			XMLUtil xml = new XMLUtil(file);
			xml.prologo();
			divisor.salvar(xml);
			xml.close();
			Util.mensagem(this, Mensagens.getString("label.salvo_modelo"));
		} catch (Exception ex) {
			Util.stackTraceAndMessage("SALVAR MODELO", ex, this);
		}
	}

	private void aplicarModelo(File file) {
		try {
			Obj raizObj = XML.processarObj(file);
			Fichario fichario = new Fichario(this, raiz);
			fichario.setSize(new Dimension(getSize()));
			fichario.adicionarOuvinte(ficharioListener);
			fichario.addAba("label.objetos", raiz, true);
			divisor.setLeftComponent(fichario);
			fichario.aplicarLayout(raizObj.getFilho(0));
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception ex) {
			Util.stackTraceAndMessage("APLICAR MODELO", ex, this);
		}
	}

	public FicharioListener getFicharioListener() {
		return ficharioListener;
	}

	private SplitPaneListener splitPaneListener = new SplitPaneListener() {
		@Override
		public void localizacao(int i) {
			Constantes.DIV_CONTROLE = i;
		}
	};

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

	private void abrirArquivo(File file, boolean msgInexistente, boolean msgSemConteudo, boolean msgNaoLeitura) {
		if (file != null) {
			if (!file.exists()) {
				if (msgInexistente) {
					String msg = Mensagens.getString("erro.arquivo.inexistente");
					Util.mensagem(this, msg + "\n\n\n" + file.getAbsolutePath());
				}
				return;
			}

			if (file.length() == 0) {
				if (msgSemConteudo) {
					String msg = Mensagens.getString("erro.arquivo.vazio");
					Util.mensagem(this, msg + "\n\n\n" + file.getAbsolutePath());
				}
				return;
			}

			if (!file.canRead()) {
				if (msgNaoLeitura) {
					String msg = Mensagens.getString("erro.arquivo.leitura");
					Util.mensagem(this, msg + "\n\n\n" + file.getAbsolutePath());
				}
				return;
			}

			setTitle(Mensagens.getString("label.arvore"));
			divisor.setLeftComponent(PNL_PADRAO);

			try {
				raiz = XML.processar(file);
				Fichario fichario = new Fichario(this, raiz);
				fichario.setSize(new Dimension(getSize()));
				fichario.adicionarOuvinte(ficharioListener);
				fichario.addAba("label.objetos", raiz, true);
				setTitle(Mensagens.getString("label.arvore") + " - " + file.getAbsolutePath());
				divisor.setLeftComponent(fichario);
			} catch (Exception ex) {
				raiz = null;
				Util.stackTraceAndMessage("ABRIR ARQUIVO", ex, this);
			} finally {
				divisor.setDividerLocation(Constantes.DIV_CONTROLE);
			}
		}
	}

	public static class Pnl_padrao extends Panel {
		private static final long serialVersionUID = 1L;
	}
}