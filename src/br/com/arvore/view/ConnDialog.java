package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import br.com.arvore.banco.Conexao;
import br.com.arvore.comp.Button;
import br.com.arvore.comp.Label;
import br.com.arvore.comp.TextField;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class ConnDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private Button buttonCancelar = new Button("label.cancelar");
	private Button buttonOK = new Button("label.ok");
	private TextField textFieldUrl = new TextField();
	private TextField textFieldUsr = new TextField();
	private TextField textFieldPsw = new TextField();

	public ConnDialog(Formulario formulario) {
		super(formulario, true);
		setTitle(Mensagens.getString("label.conexao"));
		setSize(700, 140);
		montarLayout();
		configurar();
		setLocationRelativeTo(formulario);
		setVisible(true);
		SwingUtilities.invokeLater(() -> toFront());
	}

	private void montarLayout() {
		setLayout(new BorderLayout());

		Box container = Box.createVerticalBox();
		container.add(criarLinha("label.url", textFieldUrl));
		container.add(criarLinha("label.usr", textFieldUsr));
		container.add(criarLinha("label.psw", textFieldPsw));

		Box controle = Box.createHorizontalBox();
		controle.add(buttonCancelar);
		controle.add(buttonOK);
		container.add(controle);

		add(BorderLayout.CENTER, container);

		String url = Conexao.getValor(Constantes.URL);
		String usr = Conexao.getValor(Constantes.LOGIN);
		String psw = Conexao.getValor(Constantes.SENHA);

		textFieldUrl.setText(url);
		textFieldUsr.setText(usr);
		textFieldPsw.setText(psw);
	}

	private Box criarLinha(String chaveRotulo, JComponent componente) {
		Box box = Box.createHorizontalBox();

		Label label = new Label(chaveRotulo);
		label.setPreferredSize(new Dimension(70, 0));
		label.setMinimumSize(new Dimension(70, 0));

		box.add(label);
		box.add(componente);

		return box;
	}

	private void configurar() {
		buttonCancelar.addActionListener(e -> dispose());
		buttonOK.addActionListener(e -> config());
	}

	private void config() {
		Conexao.setValor(Constantes.URL, textFieldUrl.getText());
		Conexao.setValor(Constantes.LOGIN, textFieldUsr.getText());
		Conexao.setValor(Constantes.SENHA, textFieldPsw.getText());

		try {
			Conexao.close();
			Conexao.getConnection();
			dispose();
		} catch (Exception ex) {
			Util.stackTraceAndMessage("ConnDialog.config()", ex, this);
		}
	}
}