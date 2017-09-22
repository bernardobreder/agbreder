package com.age;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe cliente para se comunicar com um agente
 * 
 * @author bernardobreder
 * 
 */
public class AgeClient {

	/** Socket de conexão */
	private Socket socket;

	/**
	 * Construtor
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public AgeClient() {
		for (;;) {
			try {
				socket = new Socket("localhost", 9889);
				break;
			} catch (IOException e) {
				System.err.println("Try to connect with localhost:9889");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	/**
	 * Requisita uma página
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public byte[] requestPage(String name) throws IOException {
		DataInputStream in = this.send("get app " + name);
		if (in.readByte() == 0) {
			return null;
		}
		int size = in.readInt();
		byte[] bytes = new byte[size];
		in.read(bytes);
		return bytes;
	}

	/**
	 * Envia um comando
	 * 
	 * @param command
	 * @return bytes de resposta
	 * @throws IOException
	 */
	private DataInputStream send(String command) throws IOException {
		OutputStream output = socket.getOutputStream();
		output.write(command.getBytes("utf-8"));
		output.write('\n');
		output.flush();
		return new DataInputStream(socket.getInputStream());
	}

	/**
	 * Fecha a sessão
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		OutputStream output = socket.getOutputStream();
		output.write("quit\n".getBytes());
		output.flush();
		socket.close();
	}

}
