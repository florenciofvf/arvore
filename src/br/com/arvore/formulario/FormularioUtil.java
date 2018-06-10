package br.com.arvore.formulario;

import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.ButtonGroup;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import br.com.arvore.banco.Conexao;
import br.com.arvore.comp.Menu;
import br.com.arvore.util.Util;

public class FormularioUtil {
	private FormularioUtil() {
	}

	public static void configMAC(Formulario formulario) {
		if (System.getProperty("os.name").startsWith("Mac OS")) {
			try {
				Class<?> classe = Class.forName("com.apple.eawt.FullScreenUtilities");
				Method method = classe.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE);
				method.invoke(classe, formulario, true);
			} catch (Exception ex) {
				Util.stackTraceAndMessage(formulario.getClass().getName() + ".setWindowCanFullScreen()", ex,
						formulario);
			}
		}
	}

	public static void fechar(Formulario formulario) {
		try {
			Conexao.close();
		} catch (Exception ex) {
			Util.stackTraceAndMessage(formulario.getClass().getName() + ".fechar()", ex, formulario);
		}
	}

	public static void menuAparencia(Formulario formulario, Menu menu) {
		LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
		ButtonGroup grupo = new ButtonGroup();

		for (LookAndFeelInfo info : installedLookAndFeels) {
			ItemLAF item = new ItemLAF(formulario, info);
			grupo.add(item);
			menu.add(item);
		}
	}
}