package br.com.arvore.util;

public class Constantes {
	public static final String ESTRATEGIA_PARAMETRO = "estrategia-parametro";
	public static final String ICONE_MANTER_VAZIO = "icone-manter-vazio";
	public static final String PESQUISA_POPUP = "pesquisa-por-popup";
	public static final String NOME_ARQUIVO_PADRAO = "default.xml";
	public static final String DESABILITADO = "desabilitado";
	public static final String MANTER_VAZIO = "manter-vazio";
	public static final String OBSERVACAO = "observacao";
	public static final String DESCRICAO = "descricao";
	public static final String SUB_ICONE = "sub-icone";
	public static final String PSMT_META = "PSMT_META";
	public static final String INST_ARVORE = "arvore";
	public static final String INST_TABELA = "tabela";
	public static final String INST_UPDATE = "update";
	public static final String INST_DELETE = "delete";
	public static final String INST_INSERT = "insert";
	public static final String PSMT_SET = "PSMT_SET";
	public static final String OBJETO = "objeto";
	public static final String DRIVER = "driver";
	public static final String TITULO = "titulo";
	public static final String LINHA = "Linha";
	public static final String LOGIN = "login";
	public static final String ICONE = "icone";
	public static final String SENHA = "senha";
	public static final String URL = "url";

	public static final boolean INFLAR_ANTECIPADO = false;
	public static final byte ESTRATEGIA_SUBSTITUIR = 0;
	public static final int LARGURA_ICONE_ORDENAR = 20;
	public static final byte ESTRATEGIA_PSMT_META = 1;
	public static final byte ESTRATEGIA_PSMT_SET = 2;
	public static final int LAR_SPLIT = 400;
	public static byte ESTRATEGIA_PARAMS;
	public static final int DOIS = 2;

	private Constantes() {
	}

	public static void configEstrategiaParametros(String s) {
		if (PSMT_SET.equalsIgnoreCase(s)) {
			ESTRATEGIA_PARAMS = ESTRATEGIA_PSMT_SET;

		} else if (PSMT_META.equalsIgnoreCase(s)) {
			ESTRATEGIA_PARAMS = ESTRATEGIA_PSMT_META;

		} else {
			ESTRATEGIA_PARAMS = ESTRATEGIA_SUBSTITUIR;
		}
	}
}