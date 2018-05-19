package br.com.arvore.xml;

import java.io.File;
import java.lang.reflect.Method;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.arvore.Objeto;
import br.com.arvore.util.ArvoreUtil;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Util;

public class XML {

	public static Objeto processar(File file) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setXIncludeAware(true);

		SAXParser parser = factory.newSAXParser();

		XMLHandler handler = new XMLHandler();
		parser.parse(file, handler);
		return handler.getRaiz();
	}
}

class XMLHandler extends DefaultHandler {
	private final String[][] MATRIZ = { { Constantes.INST_SUB_TABELA, "setInstrucaoSubTabela" },
			{ Constantes.INST_SUB_UPDATE, "setInstrucaoSubUpdate" },
			{ Constantes.INST_SUB_DELETE, "setInstrucaoSubDelete" },
			{ Constantes.INST_SUB_INSERT, "setInstrucaoSubInsert" }, { Constantes.SUB_OBSERVACAO, "setSubObservacao" },
			{ Constantes.SUB_COMENTARIO, "setSubComentario" }, { Constantes.INST_ARVORE, "setInstrucaoArvore" },
			{ Constantes.INST_TABELA, "setInstrucaoTabela" }, { Constantes.INST_UPDATE, "setInstrucaoUpdate" },
			{ Constantes.INST_DELETE, "setInstrucaoDelete" }, { Constantes.INST_INSERT, "setInstrucaoInsert" },
			{ Constantes.SUB_DESCRICAO, "setSubDescricao" }, { Constantes.OBSERVACAO, "setObservacao" },
			{ Constantes.COMENTARIO, "setComentario" }, { Constantes.SUB_ALERTA, "setSubAlerta" },
			{ Constantes.DESCRICAO, "setDescricao" }, { Constantes.ALERTA, "setAlerta" } };
	private final StringBuilder builder = new StringBuilder();
	private Objeto selecionado;
	private Objeto raiz;

	private String getNomeMetodo(String s) {
		for (String[] linha : MATRIZ) {
			if (linha[0].equals(s)) {
				return linha[1];
			}
		}

		return null;
	}

	public Objeto getRaiz() {
		ArvoreUtil.validarDependencia(raiz);
		return raiz;
	}

	private void limpar() {
		if (builder.length() > 0) {
			builder.delete(0, builder.length());
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (Constantes.OBJETO.equals(qName)) {
			String iconeManterVazio = attributes.getValue(Constantes.ICONE_MANTER_VAZIO);
			String pesquisaPopup = attributes.getValue(Constantes.PESQUISA_POPUP);
			String desabilitado = attributes.getValue(Constantes.DESABILITADO);
			String manterVazio = attributes.getValue(Constantes.MANTER_VAZIO);
			String subIcone = attributes.getValue(Constantes.SUB_ICONE);
			String titulo = attributes.getValue(Constantes.TITULO);
			String icone = attributes.getValue(Constantes.ICONE);

			Objeto objeto = new Objeto(titulo);
			objeto.setPesquisaPopup(Boolean.parseBoolean(pesquisaPopup));
			objeto.setDesabilitado(Boolean.parseBoolean(desabilitado));
			objeto.setManterVazio(Boolean.parseBoolean(manterVazio));
			objeto.setNomeIconeManterVazio(iconeManterVazio);
			objeto.setNomeSubIcone(subIcone);
			objeto.setNomeIcone(icone);

			if (raiz == null) {
				String estrategia = attributes.getValue(Constantes.ESTRATEGIA_PARAMETRO);
				Constantes.configEstrategiaParametros(estrategia);

				raiz = objeto;
			} else {
				selecionado.add(objeto);
			}

			selecionado = objeto;
		} else if (getNomeMetodo(qName) != null) {
			limpar();
		}
	}

	private void set(String metodoSet) {
		String string = builder.toString();

		if (!Util.estaVazio(string)) {
			try {
				Method method = selecionado.getClass().getDeclaredMethod(metodoSet, String.class);
				method.invoke(selecionado, string.trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (Constantes.OBJETO.equals(qName)) {
			selecionado = selecionado.getPai();

		} else {
			String metodo = getNomeMetodo(qName);

			if (metodo != null) {
				set(metodo);
				limpar();
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		builder.append(new String(ch, start, length));
	}
}