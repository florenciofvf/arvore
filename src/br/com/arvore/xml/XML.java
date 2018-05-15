package br.com.arvore.xml;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.arvore.Objeto;
import br.com.arvore.util.Constantes;
import br.com.arvore.util.Util;

public class XML {

	public static Objeto processar(File file) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setXIncludeAware(true);

		SAXParser parser = factory.newSAXParser();

		try {
			XMLHandler handler = new XMLHandler();
			parser.parse(file, handler);
			return handler.getRaiz();
		} catch (Exception ex) {
			Util.stackTraceAndMessage("XML.processar()", ex, null);
			throw new IllegalStateException();
		}
	}
}

class XMLHandler extends DefaultHandler {
	private final StringBuilder builder = new StringBuilder();
	private Objeto selecionado;
	private Objeto raiz;

	public Objeto getRaiz() {
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
			objeto.setNomeSubIcone(subIcone);
			objeto.setNomeIcone(icone);

			if (raiz == null) {
				String estrategia = attributes.getValue(Constantes.ESTRATEGIA_PARAMETRO);
				Constantes.setEstrategia(estrategia);

				raiz = objeto;
			} else {
				selecionado.add(objeto);
			}

			selecionado = objeto;
		} else if (Constantes.SQL.equals(qName)) {
			limpar();
		} else if (Constantes.SEL.equals(qName)) {
			limpar();
		} else if (Constantes.DEL.equals(qName)) {
			limpar();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (Constantes.OBJETO.equals(qName)) {
			selecionado = selecionado.getPai();

		} else if (Constantes.SQL.equals(qName)) {
			String string = builder.toString();

			if (!Util.estaVazio(string)) {
				selecionado.setConsulta(string.trim());
			}

			limpar();
		} else if (Constantes.SEL.equals(qName)) {
			String string = builder.toString();

			if (!Util.estaVazio(string)) {
				selecionado.setPesquisa(string.trim());
			}

			limpar();
		} else if (Constantes.DEL.equals(qName)) {
			String string = builder.toString();

			if (!Util.estaVazio(string)) {
				selecionado.setDeletar(string.trim());
			}

			limpar();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		builder.append(new String(ch, start, length));
	}
}