package br.com.arvore.formulario;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import br.com.arvore.Objeto;
import br.com.arvore.comp.Menu;
import br.com.arvore.comp.MenuItem;
import br.com.arvore.comp.SplitPane;
import br.com.arvore.comp.SplitPaneListener;
import br.com.arvore.container.Container;
import br.com.arvore.controle.Controle;
import br.com.arvore.dialogo.DialogoConexao;
import br.com.arvore.divisor.Divisor;
import br.com.arvore.fichario.FicharioListener;
import br.com.arvore.fichariol.FicharioL;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Icones;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;
import br.com.arvore.xml.XML;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private final SplitPane splitPane = new SplitPane();
	private final MenuPrincipal menuPrincipal = new MenuPrincipal();
	private final FicharioL ficharioLayout;
	private final Controle controle;
	private Objeto raiz;

	public Formulario() {
		setTitle(Mensagens.getString("label.arvore"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ficharioLayout = new FicharioL(this);
		Constantes.DIV_CONTROLE = 1000;
		controle = new Controle(this);
		setLayout(new BorderLayout());
		setJMenuBar(menuPrincipal);
		setSize(1000, 700);
		montarLayout();
		configurar();
	}

	private void configurar() {
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				abrirArquivo(new File(Constantes.NOME_ARQUIVO_PADRAO), false, false, false);
				splitPane.setDividerLocation(Constantes.DIV_CONTROLE);
				splitPane.setListener(splitPaneListener);
				controle.selecionadoObjeto(null);
			};

			public void windowClosing(WindowEvent e) {
				FormularioUtil.fechar(Formulario.this);
			};
		});
	}

	public Objeto getRaiz() {
		return raiz;
	}

	private void montarLayout() {
		splitPane.setOrientation(Divisor.VERTICAL_SPLIT);
		splitPane.setLeftComponent(ficharioLayout);
		splitPane.setRightComponent(controle);
		add(BorderLayout.CENTER, splitPane);
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
			controle.selecionadoObjeto(null);
			ficharioLayout.limpar();

			try {
				raiz = XML.processar(file);
				setTitle(Mensagens.getString("label.arvore") + " - " + file.getAbsolutePath());
				splitPane.setDividerLocation(Constantes.DIV_CONTROLE);
				ficharioLayout.adicionarAba(true);
			} catch (Exception ex) {
				raiz = null;
				splitPane.setDividerLocation(Constantes.DIV_CONTROLE);
				Util.stackTraceAndMessage("ABRIR ARQUIVO", ex, this);
			}
		}
	}

	private class MenuPrincipal extends JMenuBar {
		private static final long serialVersionUID = 1L;
		final MenuItem itemAplicarModelo = new MenuItem("label.aplicar_modelo", Icones.BOTTOM);
		final MenuItem itemSalvarModelo = new MenuItem("label.salvar_modelo", Icones.TOP);
		final MenuItem itemConexao = new MenuItem("label.conexao", Icones.BANCO);
		final MenuItem itemFechar = new MenuItem("label.fechar", Icones.SAIR);
		final MenuItem itemAbrir = new MenuItem("label.abrir", Icones.ABRIR);
		final Menu menuArquivo = new Menu("label.arquivo");
		final Menu menuLAF = new Menu("label.aparencia");

		MenuPrincipal() {
			itemAbrir.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));
			FormularioUtil.menuAparencia(Formulario.this, menuLAF);

			menuArquivo.add(itemAbrir);
			menuArquivo.addSeparator();
			menuArquivo.add(itemConexao);
			menuArquivo.addSeparator();
			menuArquivo.add(itemSalvarModelo);
			menuArquivo.add(itemAplicarModelo);
			menuArquivo.addSeparator();
			menuArquivo.add(itemFechar);
			add(menuArquivo);
			add(menuLAF);
			configurar();
		}

		private void configurar() {
			itemFechar.addActionListener(
					e -> dispatchEvent(new WindowEvent(Formulario.this, WindowEvent.WINDOW_CLOSING)));
			itemConexao.addActionListener(e -> new DialogoConexao(Formulario.this));
			FormularioUtil.configMAC(Formulario.this);

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
	}
}