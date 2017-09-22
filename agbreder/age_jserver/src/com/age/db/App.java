package com.age.db;

/**
 * Estrutura de aplicação
 * 
 * @author bernardobreder
 * 
 */
public class App {

	/** Código da aplicação */
	private int id;
	/** Nome da Aplicação */
	private String name;
	/** Número de Revisão */
	private int revision;
	/** Icone da aplicação */
	private byte[] icon;
	/** Dados da Aplicação */
	private byte[] app;
	/** Dados do Banco de Dados */
	private byte[] database;

	/**
	 * Construtor
	 * 
	 * @param id
	 * @param name
	 * @param revision
	 * @param icon
	 * @param app
	 * @param database
	 */
	public App(int id, String name, int revision, byte[] icon, byte[] app, byte[] database) {
		super();
		this.id = id;
		this.name = name;
		this.revision = revision;
		this.icon = icon;
		this.app = app;
		this.database = database;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the revision
	 */
	public int getRevision() {
		return revision;
	}

	/**
	 * @param revision
	 *            the revision to set
	 */
	public void setRevision(int revision) {
		this.revision = revision;
	}

	/**
	 * @return the app
	 */
	public byte[] getApp() {
		return app;
	}

	/**
	 * @param app
	 *            the app to set
	 */
	public void setApp(byte[] app) {
		this.app = app;
	}

	/**
	 * @return the database
	 */
	public byte[] getDatabase() {
		return database;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	public void setDatabase(byte[] database) {
		this.database = database;
	}

	/**
	 * @return the icon
	 */
	public byte[] getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

}
