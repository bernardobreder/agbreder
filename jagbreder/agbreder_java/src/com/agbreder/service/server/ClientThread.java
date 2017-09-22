package com.agbreder.service.server;

import java.io.StringReader;
import java.net.Socket;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.agbreder.service.server.request.AGBPageRequest;
import com.agbreder.service.server.request.AGBRequest;
import com.agbreder.util.RequestInputStream;
import com.agbreder.util.RequestOutputStream;

/**
 * Classe que responde a uma requisição vinda de um outro usuário
 * 
 * @author bernardobreder
 */
public class ClientThread extends Thread {
	
	private final Socket socket;
	
	/**
	 * Construtor
	 * 
	 * @param socket
	 */
	public ClientThread(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void run() {
		try {
			RequestInputStream dataInput =
				new RequestInputStream(socket.getInputStream());
			String xml = dataInput.readZipUTF();
			SAXReader reader = new SAXReader();
			Document document = reader.read(new StringReader(xml));
			Element rootElem = document.getRootElement();
			AGBRequest request = new AGBPageRequest(rootElem);
			document = request.response();
			RequestOutputStream output =
				new RequestOutputStream(socket.getOutputStream());
			output.writeZipUTF(document.asXML());
			socket.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
