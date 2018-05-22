package br.com.arvore.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Icones {
	private static final Map<String, Icon> MAPA_ICONES = new HashMap<>();

	public static final Icon DESC_NUMERO = criarImagem("desc_numero");
	public static final Icon ASC_NUMERO = criarImagem("asc_numero");
	public static final Icon OBSERVACAO = criarImagem("tag_yellow");
	public static final Icon DESC_TEXTO = criarImagem("desc_texto");
	public static final Icon DESCONECTA = criarImagem("desconecta");
	public static final Icon ASC_TEXTO = criarImagem("asc_texto");
	public static final Icon LOCALIZAR = criarImagem("localizar");
	public static final Icon COMENTARIO = criarImagem("comment");
	public static final Icon ATUALIZAR = criarImagem("refresh");
	public static final Icon EXCLUIR = criarImagem("lixeira");
	public static final Icon UPDATE = criarImagem("update");
	public static final Icon ARVORE = criarImagem("arvore");
	public static final Icon TABELA = criarImagem("tabela");
	public static final Icon ALERTA = criarImagem("alerta");
	public static final Icon DIALOG = criarImagem("dialog");
	public static final Icon CRIAR = criarImagem("create");
	public static final Icon BANCO = criarImagem("banco");
	public static final Icon ABRIR = criarImagem("open");
	public static final Icon SAIR = criarImagem("sair");
	public static final Icon INFO = criarImagem("info");

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