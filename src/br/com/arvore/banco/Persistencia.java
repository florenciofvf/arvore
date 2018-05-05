package br.com.arvore.banco;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import br.com.arvore.Arg;
import br.com.arvore.Objeto;

public class Persistencia {

	public static List<Objeto> getObjetos(Objeto objeto) throws Exception {
		Connection conn = Conexao.getConnection();
		PreparedStatement psmt = conn.prepareStatement(objeto.getConsulta());
		setParametros(objeto.getPai(), psmt);

		ResultSet rs = psmt.executeQuery();
		List<Objeto> objetos = coletar(rs);

		rs.close();
		psmt.close();

		return objetos;
	}

	private static void setParametros(Objeto objeto, PreparedStatement psmt) throws Exception {
		ParameterMetaData pmd = psmt.getParameterMetaData();

		int parametros = pmd.getParameterCount();

		for (int i = 1; i <= parametros; i++) {
			Arg arg = objeto.getArgs()[i - 1];
			arg.set(psmt, i);
		}
	}

	public static List<Objeto> coletar(ResultSet rs) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		List<Objeto> objetos = new ArrayList<>();
		int colunas = rsmd.getColumnCount();

		while (rs.next()) {
			Objeto objeto = new Objeto(rs.getString(1));

			Arg[] args = new Arg[colunas - 1];

			for (int i = 2; i <= colunas; i++) {
				Arg arg = Arg.criar(rsmd.getColumnName(i));
				arg.get(rs, i);
				args[i - 2] = arg;
			}

			objeto.setArgs(args);
			objetos.add(objeto);
		}

		return objetos;
	}
}