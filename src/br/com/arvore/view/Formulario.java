package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;

import javax.swing.JFrame;

import br.com.arvore.Objeto;
import br.com.arvore.banco.Conexao;
import br.com.arvore.comp.Arvore;
import br.com.arvore.comp.ArvoreListener;
import br.com.arvore.comp.ScrollPane;
import br.com.arvore.comp.TabbedPane;
import br.com.arvore.modelo.ModeloArvore;
import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;
import br.com.arvore.xml.XML;

public class Formulario extends JFrame implements ArvoreListener {
	private static final long serialVersionUID = 1L;
	private final TabbedPane fichario = new TabbedPane();
	private final Popup popup = new Popup();
	private final Arvore arvoreInflados;
	private final Arvore arvoreObjetos;
	private final Objeto raizInflados;
	private final Objeto raizObjetos;
	private Objeto selecionadoPopup;

	public Formulario(File file) throws Exception {
		setTitle(Mensagens.getString("label.arvore"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		raizObjetos = XML.processar(file);
		raizInflados = raizObjetos.clonar();
		raizInflados.inflar();
		arvoreInflados = new Arvore(new ModeloArvore(raizInflados), this);
		arvoreObjetos = new Arvore(new ModeloArvore(raizObjetos), null);
		setSize(500, 500);
		montarLayout();
		configuracoes();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void configuracoes() {
		if (System.getProperty("os.name").startsWith("Mac OS")) {
			try {
				Class<?> classe = Class.forName("com.apple.eawt.FullScreenUtilities");
				Method method = classe.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE);
				method.invoke(classe, this, true);
			} catch (Exception e) {
				String msg = Util.getStackTrace(getClass().getName() + ".setWindowCanFullScreen()", e);
				Util.mensagem(this, msg);
			}
		}

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					Conexao.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			};
		});

		popup.itemAtualizar.addActionListener(e -> {
			try {
				selecionadoPopup.inflar();
				ArvoreUtil.atualizar(arvoreInflados, selecionadoPopup);
			} catch (Exception ex) {
				String msg = Util.getStackTrace("ATUALIZAR", ex);
				Util.mensagem(this, msg);
			}
		});
	}

	private void montarLayout() {
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, fichario);
		fichario.addTab("label.inflados", new ScrollPane(arvoreInflados));
		fichario.addTab("label.objetos", new ScrollPane(arvoreObjetos));
	}

	@Override
	public void exibirPopup(Arvore arvore, Objeto selecionado, MouseEvent e) {
		selecionadoPopup = selecionado;
		popup.show(arvore, e.getX(), e.getY());
	}

	@Override
	public void clicado(Objeto objeto) {
	}
}