package com.age.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Conexão SqlLite
 * 
 * @author bernardobreder
 * 
 */
public class SqlLiteConnection {

	/** Connection */
	private Connection conn;
	/** Lock */
	private Lock lock = new ReentrantLock();

	/**
	 * Construtor
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public SqlLiteConnection() throws SQLException {
		conn = this.connect();
		Statement stat = conn.createStatement();
		stat.executeUpdate("drop table if exists app");
		stat.executeUpdate("create table app (" + "id integer not null primary key, " + "name varchar(255) not null unique, " + "revision integer not null, " + "icon binary not null, " + "app binary not null, " + "database binary not null)");
		stat.close();
		conn.commit();
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
		Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
		conn.setAutoCommit(false);
		return conn;
	}

	/**
	 * Insere um nova aplicação
	 * 
	 * @param name
	 * @throws SQLException
	 */
	public void insertApp(App app) throws SQLException {
		String sql = "insert into app (name, revision, icon, app, database) values (?, ?, ?, ?, ?)";
		PreparedStatement ps = this.conn.prepareStatement(sql);
		ps.setString(1, app.getName());
		ps.setInt(2, app.getRevision());
		ps.setBytes(3, app.getIcon());
		ps.setBytes(4, app.getApp());
		ps.setBytes(5, app.getDatabase());
		ps.execute();
	}

	/**
	 * Insere um nova aplicação
	 * 
	 * @param name
	 * @throws SQLException
	 */
	public void updateApp(App app) throws SQLException {
		String sql = "update app set revision = ?, icon = ?, app = ? where name = ?";
		PreparedStatement ps = this.conn.prepareStatement(sql);
		ps.setInt(1, app.getRevision());
		ps.setBytes(2, app.getIcon());
		ps.setBytes(3, app.getApp());
		ps.setString(4, app.getName());
		ps.execute();
	}

	/**
	 * Retorna a estrututa de dados de uma aplicação
	 * 
	 * @param name
	 * @throws SQLException
	 */
	public App getApp(String name) throws SQLException {
		String sql = "select id, revision, icon, app, database from app where name = ?";
		PreparedStatement ps = this.conn.prepareStatement(sql);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			int id = rs.getInt(1);
			int revision = rs.getInt(2);
			byte[] icon = rs.getBytes(3);
			byte[] app = rs.getBytes(4);
			byte[] database = rs.getBytes(5);
			return new App(id, name, revision, icon, app, database);
		} else {
			return null;
		}
	}

	/**
	 * Retorna a estrututa de dados de uma aplicação
	 * 
	 * @param name
	 * @throws SQLException
	 */
	public App updateApp(String name, int revision) throws SQLException {
		App app = this.getApp(name);
		if (app.getRevision() > revision) {
			return app;
		} else {
			return null;
		}
	}

	/**
	 * Realiza o lock
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		this.conn.commit();
	}

	/**
	 * Realiza o lock
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		this.conn.rollback();
	}

	/**
	 * Realiza o lock
	 */
	public void lock() {
		this.lock.lock();
	}

	/**
	 * Libera o lock
	 */
	public void unlock() {
		this.lock.unlock();
	}

}
