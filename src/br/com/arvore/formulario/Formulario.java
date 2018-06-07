package br.com.arvore.formulario;

import java.awt.BorderLayout;
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
import br.com.arvore.comp.Menu;
import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.SplitPaneListener;
import br.com.arvore.container.Container;
import br.com.arvore.controle.Controle;
import br.com.arvore.dialogo.DialogoConexao;
import br.com.arvore.divisor.Divisor;
import br.com.arvore.divisor.DivisorUtil;
import br.com.arvore.fichario.Fichario;
import br.com.arvore.fichario.FicharioListener;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;
import br.com.arvore.xml.XML;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private final MenuItem itemConexao = new MenuItem("label.conexao", Icones.BANCO);
	private final MenuItem itemFechar = new MenuItem("label.fechar", Icones.SAIR);
	private final MenuItem itemAbrir = new MenuItem("label.abrir", Icones.ABRIR);

	private final Menu menuAparencia = new Menu("label.aparencia");
	private final Menu menuArquivo = new Menu("label.arquivo");

	private final JMenuBar menuBar = new JMenuBar();
	private final Divisor divisor = new Divisor();
	private final Controle controle;

	public Formulario() {
		setTitle(Mensagens.getString("label.arvore"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		controle = new Controle(this);
		setLayout(new BorderLayout());
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
				divisor.setDividerLocation(0.78);
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
	}

	private void montarLayout() {
		divisor.setOrientation(Divisor.VERTICAL_SPLIT);
		divisor.setListener(splitPaneListener);
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
		menuArquivo.add(itemFechar);

		configMenuAparencia();

		itemAbrir.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));
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

	public FicharioListener getFicharioListener() {
		return ficharioListener;
	}

	private SplitPaneListener splitPaneListener = new SplitPaneListener() {
		@Override
		public void localizacao(int i) {
			Constantes.DIV_FICHARIO_CONTROLE = i;
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

			divisor.setLeftComponent(null);

			try {
				setTitle(Mensagens.getString("label.arvore"));
				Objeto raiz = XML.processar(file);
				Fichario fichario = new Fichario(this);
				fichario.adicionarOuvinte(ficharioListener);
				DivisorUtil.setLeftTrue(fichario);
				fichario.setDivisor(divisor);
				fichario.setRaiz(raiz);
				fichario.addAba("label.objetos", raiz, true);
				setTitle(Mensagens.getString("label.arvore") + " - " + file.getAbsolutePath());
				divisor.setLeftComponent(fichario);
				divisor.setDividerLocation(Constantes.DIV_FICHARIO_CONTROLE);
			} catch (Exception ex) {
				Util.stackTraceAndMessage("ABRIR ARQUIVO", ex, this);
			}
		}
	}
}