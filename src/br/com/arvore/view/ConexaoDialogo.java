package br.com.arvore.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import br.com.arvore.banco.Conexao;
import br.com.arvore.comp.Label;
import br.com.arvore.comp.TextField;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Mensagens;
import br.com.arvore.util.Util;

public class ConexaoDialogo extends Dialogo {
	private static final long serialVersionUID = 1L;
	private TextField textFieldUrl = new TextField();
	private TextField textFieldUsr = new TextField();
	private TextField textFieldPsw = new TextField();

	public ConexaoDialogo(Formulario formulario) {
		super(formulario, 700, 140, Mensagens.getString("label.conexao"));
		montarLayout();
		setVisible(true);
		SwingUtilities.invokeLater(() -> toFront());
	}

	private void montarLayout() {
		textFieldUsr.setText(Conexao.getValor(Constantes.LOGIN));
		textFieldPsw.setText(Conexao.getValor(Constantes.SENHA));
		textFieldUrl.setText(Conexao.getValor(Constantes.URL));

		Box container = Box.createVerticalBox();
		container.add(criarLinha("label.url", textFieldUrl));
		container.add(criarLinha("label.usr", textFieldUsr));
		container.add(criarLinha("label.psw", textFieldPsw));

		add(BorderLayout.CENTER, container);
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

	protected void processar() {
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