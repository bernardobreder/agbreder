package com.age;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import com.age.db.App;
import com.age.db.SqlLiteConnection;
import com.age.db.SqlLiteConnectionTest;

public class AgeServer {

	private ServerSocket server;
	private SqlLiteConnection conn;

	/**
	 * Construtor
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public AgeServer(int port) throws IOException, ClassNotFoundException, SQLException {
		conn = new SqlLiteConnection();
		this.server = new ServerSocket(port);
	}

	/**
	 * Inicia o servidor
	 */
	public void start() {
		for (;;) {
			try {
				Socket socket = this.server.accept();
				new AgeClientThread(socket, conn).start();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Inicializador
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(9889);
		SqlLiteConnectionTest conn = new SqlLiteConnectionTest();
		conn.insertApp(new App(1, "test", 1, "icon".getBytes(), "app".getBytes(), "database".getBytes()));
		for (;;) {
			try {
				Socket socket = server.accept();
				new AgeClientThread(socket, conn).start();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

}
