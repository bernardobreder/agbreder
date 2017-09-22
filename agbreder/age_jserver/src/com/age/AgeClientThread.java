package com.age;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.age.db.SqlLiteConnection;

public class AgeClientThread extends Thread {

	/** Socket */
	private final Socket socket;
	/** Acesso ao Banco de Dados */
	private final SqlLiteConnection conn;

	/**
	 * Construtor
	 * 
	 * @param socket
	 * @param conn
	 */
	public AgeClientThread(Socket socket, SqlLiteConnection conn) {
		this.socket = socket;
		this.conn = conn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			InputStream input = this.socket.getInputStream();
			AgeProcessor processor = new AgeProcessor(conn);
			for (; this.socket.isConnected();) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				for (;;) {
					int n = input.read();
					if (n == -1) {
						return;
					} else if (n < ' ') {
						break;
					}
					output.write(n);
				}
				String request = new String(output.toByteArray(), "utf-8");
				OutputStream stream = socket.getOutputStream();
				if (request.equals("quit")) {
					DataOutputStream data = new DataOutputStream(stream);
					data.writeInt(0);
					this.socket.close();
					break;
				}
				processor.execute(request, stream);

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
