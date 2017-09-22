package com.age.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Banco de Dados em memoria
 * 
 * @author bernardobreder
 * 
 */
public class SqlLiteConnectionTest extends SqlLiteConnection {

	/**
	 * Construtor
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public SqlLiteConnectionTest() throws ClassNotFoundException, SQLException {
		super();
	}
	
	/**
	 * Cria uma conexão
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected Connection connect() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}
		Connection conn = DriverManager.getConnection("jdbc:sqlite:");
		conn.setAutoCommit(false);
		return conn;
	}


}
