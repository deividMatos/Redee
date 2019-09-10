package br.com.ernestoborges.redee.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Repository;

import br.com.ernestoborges.redee.model.ModeloTO;
import br.com.ernestoborges.redee.model.PastaTag;
import br.com.ernestoborges.redee.model.Valores;
import br.com.ernestoborges.redee.util.ConnectionFactory;

@Repository
public class RedeeRepository {

	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Metodo que fara o insert de Tese no banco de dados
	 * 
	 * @param m     parametro que tras os dados do modelo para insert de tese
	 * @param texto parametro que tras o texto informativo da tese
	 * @since 2019 08 29
	 */
	public void insertTese(ModeloTO m, String texto) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO dba.redtes (tesmodelo, testextos, tespastas, tescaddat)");
		sql.append("VALUES ('" + m.getModelo() + "', '" + texto + "', '" + m.getPasta() + "', '"+df.format(new Date())+"')");
		String base = "";
		if (m.getPasta().endsWith("301")) {
			base = "LFM";
		} else if (m.getPasta().endsWith("201")) {
			base = "LFM_BG";
		} else if (m.getPasta().endsWith("101")) {
			base = "LFM_DPVAT";
		}
		try {
			con = ConnectionFactory.getConnection(base);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());
		} catch (Exception e) {
			System.out.println("Erro RedeeService[01]: " + e.getMessage());
		}

	}

	/**
	 * Metodo que fara o insert de Tags no banco de dados
	 * 
	 * @param tag parametro que tras as informações da pasta como o numero
	 * @param v   objeto tipo Valores que trás as informações da tag
	 * @since 2019 08 29
	 */
	public void insertTag(PastaTag tag, Valores v) {
		
		int sequeNum = 0;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT MAX(redsequen) AS ultsequen FROM dba.prored");
			if (rs.next()) {
				sequeNum = rs.getInt("ultsequen");
			} else {
				sequeNum = 1;
			}
			++sequeNum;
		} catch (SQLException e) {
			System.err.println("|------> [RedeeService] [04] " + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println("|------> [RedeeService] [04] " + e.getMessage());
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO dba.prored (redsequen, redpastas, rednomtag, redvalors, ");
		sql.append("  reddestag, redareass, reddocids, redlogins,reddatass)");
		sql.append(" VALUES ( ");
		sql.append(sequeNum+", ");
		sql.append(tag.getPasta()+", ");
		sql.append(v.getTag()+", ");
		sql.append(v.getValor()+", ");
		sql.append(v.getNome()+", ");
		sql.append(v.getArea()+", ");
		sql.append(v.getDocId()+", ");
		sql.append(v.getLogin()+", ");
		sql.append("'"+df.format(new Date())+"' ");
		sql.append(")");
		String base = "";
		if (tag.getPasta().endsWith("301")) {
			base = "LFM";
		} else if (tag.getPasta().endsWith("201")) {
			base = "LFM_BG";
		} else if (tag.getPasta().endsWith("101")) {
			base = "LFM_DPVAT";
		}

		try {
			con = ConnectionFactory.getConnection(base);
			stmt = con.createStatement();
			System.out.println("SEQUENCIAL SALVO: " + sequeNum);
			rs = stmt.executeQuery(sql.toString());
		} catch (Exception e) {
			System.out.println("Erro RedeeService[02]: " + e.getMessage());
		}

	}
	
	/**
	 * Metodoo que fara o update nas informacoes da tag (prored)
	 * 
	 * @param sequencial define o sequencial da dados que sera alterado
	 * @param r          define os dados do item que sera alterado
	 * @param tag        define os da tag que sera alterada
	 * @since 2019 09 02
	 */
	public void alteraTag(int sequencial, Valores r, PastaTag tag) {
		String base = "";
		if (tag.getPasta().endsWith("301")) {
			base = "LFM";
		} else if (tag.getPasta().endsWith("201")) {
			base = "LFM_BG";
		} else if (tag.getPasta().endsWith("101")) {
			base = "LFM_DPVAT";
		}
		try (Connection con = ConnectionFactory.getConnection(base)) {
			PreparedStatement pstmt = con.prepareStatement("UPDATE prored SET redvalors = ? WHERE redsequen = ? ");
			pstmt.setString(1, r.getValor());
			pstmt.setString(2, String.valueOf(sequencial));
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			System.err.println("|------> [RedeeService] [05] " + e.getMessage());
		}

	}
	
	/**
	 * Metodo que ira fazer a verificação se ja existe um cadastro na mesma pasta
	 * com a mesma tag
	 * 
	 * @param r parametro que define os dados que serao usados na pesquisa
	 * @param v parametro que define os valores das tags contidas na lista de solicitacao
	 * @return retorna o sequencial do item se caso for encontrado e se nao ele
	 *         retorna ZERO
	 * @since 2019 09 02
	 */
	public int verificaJaExiste(PastaTag r, Valores v) {
		String base = "";
		if (r.getPasta().endsWith("301")) {
			base = "LFM";
		} else if (r.getPasta().endsWith("201")) {
			base = "LFM_BG";
		} else if (r.getPasta().endsWith("101")) {
			base = "LFM_DPVAT";
		}
		
		try (Connection con = ConnectionFactory.getConnection(base)) {
			stmt = con.createStatement();
			rs = stmt.executeQuery("  select redsequen from dba.prored where redpastas = '" + r.getPasta()
					+ "' and rednomtag = '" + v.getTag() + "'");

			if (rs.next())
				return rs.getInt("redsequen");

		} catch (SQLException e) {
			System.err.println("|------> [RedeeService] [03] " + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println("|------> [RedeeService] [03] " + e.getMessage());
		}
		return 0;
	}
}
