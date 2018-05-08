package br.com.arvore.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Icones {
	private static final Map<String, Icon> MAPA_ICONES = new HashMap<>();

	public static final Icon ATUALIZAR = criarImagem("atualizar");
	public static final Icon TABELA = criarImagem("tabela");

	private static ImageIcon criarImagem(String nome) {
		try {
			URL url = Icones.class.getResource("/resources/" + nome + ".png");
			return new ImageIcon(url, nome);
		} catch (Exception e) {
			throw new IllegalStateException("Erro imagem! " + nome);
		}
	}

	public static Icon getIcon(String nome) {
		Icon icon = MAPA_ICONES.get(nome);

		if (icon == null) {
			icon = criarImagem(nome);
			MAPA_ICONES.put(nome, icon);
		}

		return icon;
	}
}