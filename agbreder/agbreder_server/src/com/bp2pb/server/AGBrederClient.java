package com.bp2pb.server;

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
public class AGBrederClient {

	/** Socket de conexão */
	private Socket socket;

	/**
	 * Construtor
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public AGBrederClient() throws IOException {
		socket = new Socket("localhost", 9889);
	}

	/**
	 * Envia um comando
	 * 
	 * @param command
	 * @return bytes de resposta
	 * @throws IOException
	 */
	public byte[] send(String command) throws IOException {
		OutputStream output = socket.getOutputStream();
		output.write(command.getBytes("utf-8"));
		output.write('\n');
		output.flush();
		DataInputStream input = new DataInputStream(socket.getInputStream());
		int size = input.readInt();
		byte[] bytes = new byte[size];
		for (int n = 0; n < size; n++) {
			bytes[n] = input.readByte();
		}
		return bytes;
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
