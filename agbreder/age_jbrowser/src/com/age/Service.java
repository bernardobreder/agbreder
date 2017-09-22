package com.age;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Service {

	public static void createHost(String host) throws IOException {
		Socket socket = createMasterSocket();
		String request = "create ";
		socket.getOutputStream().write(request.getBytes());
		socket.close();
	}

	public static void registerInboxServer(String host) throws IOException {
	}

	public static void registerProcessorServer(String host, int localPort) throws IOException {
	}

	private static Socket createMasterSocket() throws UnknownHostException, IOException {
		return new Socket("localhost", 9889);
	}

}
