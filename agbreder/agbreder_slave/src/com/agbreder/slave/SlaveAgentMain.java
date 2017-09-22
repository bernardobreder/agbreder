package com.agbreder.slave;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe que inicializa o servidor escravo
 * 
 * @author bernardobreder
 * 
 */
public class SlaveAgentMain {

	/**
	 * Inicializador
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(8998);
		} catch (IOException e) {
			System.err.println("Can not create a Slave Server in the port 8998.");
			System.exit(1);
		}
		for (;;) {
			try {
				Socket socket = server.accept();
				
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

}
