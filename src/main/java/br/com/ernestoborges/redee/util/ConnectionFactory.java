package br.com.ernestoborges.redee.util;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import br.com.ernestoborges.redee.model.Server;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;

public final class ConnectionFactory {

	public static JtdsDataSource ds;

	/**
	 * MEtodo que fara a conexao com o banco de dados.
	 * 
	 * @param base
	 *            parametro que define a base onde sera conectado EX.: LFM,
	 *            LFM_BG...
	 * @throws SQLException Erro na busca da conexao
	 */
	public static void setConnection(String base) throws SQLException {
		JtdsDataSource ds = new JtdsDataSource();

		ds.setUser("dba");
		ds.setPassword("sql");
		ds.setServerName(loadDBA());
		ds.setDatabaseName(base);

		ConnectionFactory.ds = ds;
	}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	public static Connection getConnection(String base) throws SQLException {
		JtdsDataSource ds = new JtdsDataSource();
		ds.setUser("dba");
		ds.setPassword("sql");
		ds.setServerName(loadDBA());
		ds.setDatabaseName(base);
		return ds.getConnection();
	}
	/**
	 * Metodo que faz a leitura de XML para a identificação do banco de dados (ip)
	 * 
	 * @return retorna o ip do banco de dados
	 */
	public static String loadDBA() {
		try {
			File f = new File("\\\\10.67.0.24\\dba\\banco.xml");

			JAXBContext context = JAXBContext.newInstance(Server.class);
			Unmarshaller um = context.createUnmarshaller();
			Server server = (Server) um.unmarshal(f);
			return server.getIp();
		} catch (Exception e) {
			System.err.println("Erro ao ler arquivo de configuração");
			return null;
		}
	}
}