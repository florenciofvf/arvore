package br.com.arvore.dialogo;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import br.com.arvore.compnte.Button;
import br.com.arvore.compnte.PanelCenter;

public abstract class Dialogo extends JDialog {
	private static final long serialVersionUID = 1L;
	private Button buttonCancelar = new Button("label.cancelar");
	private Button buttonOK = new Button("label.ok");

	public Dialogo(Frame frame, int largura, int altura, String titulo, boolean buttonProcessar) {
		super(frame, true);

		PanelCenter botoes = new PanelCenter(buttonCancelar);

		if (buttonProcessar) {
			botoes.add(buttonOK);
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(largura, altura);
		setLayout(new BorderLayout());
		add(BorderLayout.SOUTH, botoes);
		setLocationRelativeTo(frame);
		setActionESC(this);
		setTitle(titulo);
		configurar();
	}

	private void configurar() {
		buttonCancelar.addActionListener(e -> dispose());
		buttonOK.addActionListener(e -> processar());
	}

	protected abstract void processar();

	private void setActionESC(JDialog dialog) {
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
}