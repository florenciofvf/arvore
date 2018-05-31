package br.com.arvore.util;

public class Constantes {
	public static final String ESTRATEGIA_PARAMETRO = "estrategia-parametro";
	public static final String INST_SUB_OBJETO_TITULO = "sub-objeto-titulo";
	public static final String INST_SUB_OBJETO_ARVORE = "sub-objeto-arvore";
	public static final String ICONE_MANTER_VAZIO = "icone-manter-vazio";
	public static final String PESQUISA_POPUP = "pesquisa-por-popup";
	public static final String NOME_ARQUIVO_PADRAO = "default.xml";
	public static final String SUB_OBSERVACAO = "sub-observacao";
	public static final String SUB_COMENTARIO = "sub-comentario";
	public static final String SUB_DESCRICAO = "sub-descricao";
	public static final String INST_SUB_TABELA = "sub-tabela";
	public static final String INST_SUB_UPDATE = "sub-update";
	public static final String INST_SUB_DELETE = "sub-delete";
	public static final String INST_SUB_INSERT = "sub-insert";
	public static final String DESABILITADO = "desabilitado";
	public static final String MANTER_VAZIO = "manter-vazio";
	public static final String SUB_ALERTA = "sub-alerta";
	public static final String COMENTARIO = "comentario";
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
	public static final String UM_PIXEL = "um_pixel";
	public static final String ALERTA = "alerta";
	public static final String OBJETO = "objeto";
	public static final String DRIVER = "driver";
	public static final String TITULO = "titulo";
	public static final String LINHA = "Linha";
	public static final String LOGIN = "login";
	public static final String ICONE = "icone";
	public static final String SENHA = "senha";
	public static final String URL = "url";

	public static final boolean INFLAR_ANTECIPADO = false;
	public static final boolean INFLAR_DESATIVADO = false;
	public static final byte ORGANIZACAO_HORIZONTAL = 2;
	public static final byte ESTRATEGIA_SUBSTITUIR = 0;
	public static final int LARGURA_ICONE_ORDENAR = 20;
	public static final byte ORGANIZACAO_VERTICAL = 1;
	public static final byte ESTRATEGIA_PSMT_META = 1;
	public static final byte ESTRATEGIA_PSMT_SET = 2;
	public static final byte ORGANIZACAO_NORMAL = 0;
	public static int DIV_FICHARIO_CONTROLE = 500;
	public static int DIV_ARVORE_TABELA = 360;
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