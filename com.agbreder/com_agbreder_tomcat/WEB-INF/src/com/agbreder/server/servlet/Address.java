package com.agbreder.server.servlet;

import java.io.Serializable;

public class Address implements Serializable {
	
	private static final long serialVersionUID = -6849794470754667710L;
	
	private String id;
	
	private String host;
	
	private int port;
	
	public Address(String id, String host, int port) {
		super();
		this.id = id;
		this.host = host;
		this.port = port;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id
	 *        the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * @param host
	 *        the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @param port
	 *        the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
}
