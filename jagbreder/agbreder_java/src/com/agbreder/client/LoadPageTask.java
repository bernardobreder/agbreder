package com.agbreder.client;

import java.awt.Component;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.agbreder.client.util.task.ReadTask;
import com.agbreder.service.gui.AGBPageNode;
import com.agbreder.service.server.SlaveServerThread;
import com.agbreder.service.server.request.AGBPageRequest;
import com.agbreder.util.RequestInputStream;
import com.agbreder.util.RequestOutputStream;

/**
 * Carrega uma página
 * 
 * @author bernardobreder
 */
public class LoadPageTask extends ReadTask {
	
	private static final String AGB = "agb://";
	
	/** Caminho */
	private String url;
	
	private AGBPageNode node;
	
	/**
	 * Construtor
	 * 
	 * @param url
	 */
	public LoadPageTask(String url) {
		this.url = url;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void action() throws Throwable {
		if (url.startsWith(AGB)) {
			url = url.substring(AGB.length());
			this.agbPerform();
		} else {
			this.agbPerform();
		}
	}
	
	/**
	 * @throws Exception
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void agbPerform() throws Exception, UnknownHostException,
		IOException, DocumentException {
		String project = "";
		String page = "index";
		int index = url.indexOf('/');
		if (index >= 0) {
			project = url.substring(0, index);
			page = url.substring(index);
		} else {
			project = url;
		}
		AGBPageRequest request = new AGBPageRequest(project, page);
		Document document = request.request();
		Socket socket = new Socket("localhost", SlaveServerThread.PORT);
		try {
			RequestOutputStream output =
				new RequestOutputStream(socket.getOutputStream());
			output.writeZipUTF(document.asXML());
			SAXReader reader = new SAXReader();
			socket.getOutputStream().flush();
			RequestInputStream input =
				new RequestInputStream(socket.getInputStream());
			document = reader.read(new StringReader(input.readZipUTF()));
		} finally {
			socket.close();
		}
		this.node = new AGBPageNode();
		node.load(document.getRootElement());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateUI() {
		Component component = this.node.build();
		BrowserFrame.getInstance().setPage(component);
	}
	
}
