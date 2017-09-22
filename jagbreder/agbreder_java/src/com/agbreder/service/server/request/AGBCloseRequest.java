package com.agbreder.service.server.request;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Realiza uma requisição de página
 * 
 * @author bernardobreder
 */
public class AGBCloseRequest extends AGBRequest {
	
	private String project;
	
	private String page;
	
	/**
	 * Constroi a requisição
	 * 
	 * @param rootElem
	 */
	public AGBCloseRequest(Element rootElem) {
		this.project = rootElem.attributeValue("project");
		this.page = rootElem.attributeValue("page");
	}
	
	/**
	 * Construtor
	 * 
	 * @param project
	 * @param page
	 */
	public AGBCloseRequest(String project, String page) {
		super();
		this.project = project;
		this.page = page;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Document request() throws Exception {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("page");
		root.addAttribute("project", this.project);
		root.addAttribute("page", this.page);
		return document;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Document response() throws Exception {
		SAXReader reader = new SAXReader();
		Document document =
			reader.read(this.getClass().getResourceAsStream(
				"/com/agbreder/test/xml/page1.xml"));
		return document;
	}
	
}
