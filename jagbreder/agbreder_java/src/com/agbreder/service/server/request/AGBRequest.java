package com.agbreder.service.server.request;

import org.dom4j.Document;

/**
 * Requisição de um cliente
 * 
 * @author bernardobreder
 */
public abstract class AGBRequest {
	
	/**
	 * Executa a requisição
	 * 
	 * @return documento xml
	 * @throws Exception
	 */
	public abstract Document response() throws Exception;
	
	/**
	 * Executa a requisição
	 * 
	 * @return documento xml
	 * @throws Exception
	 */
	public abstract Document request() throws Exception;
	
}
