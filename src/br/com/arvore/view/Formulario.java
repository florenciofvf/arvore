package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;

import javax.swing.JFrame;

import br.com.arvore.banco.Conexao;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private final Fichario fichario;

	public Formulario(File file) throws Exception {
		setTitle(Mensagens.getString("label.arvore"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		fichario = new Fichario(file);
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
	}

	private void montarLayout() {
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, fichario);
	}
}