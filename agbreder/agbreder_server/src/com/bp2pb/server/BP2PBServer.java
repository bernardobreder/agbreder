package com.bp2pb.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import com.bp2pb.server.database.SqlLiteConnection;

public class BP2PBServer {

	private ServerSocket server;
	private SqlLiteConnection conn;

	/**
	 * Construtor
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public BP2PBServer(int port) throws IOException, ClassNotFoundException, SQLException {
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
				new BP2PBClientThread(socket, conn).start();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

}
