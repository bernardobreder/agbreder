package com.agbreder.api.db;

import java.sql.SQLException;
import java.util.List;

/**
 * Gerenciador de banco de dados. Essa classe é responsável por gerenciar os
 * recursos de banco de dados de uma aplicação
 * 
 * @author bernardobreder
 */
public interface IDBManager {
	
	/**
	 * Recupere a entidade de banco de dados
	 * 
	 * @param query
	 * @return entidade de banco de dados
	 * @throws SQLException
	 */
	public List<IEntity> query(IQuery query) throws SQLException;
	
	/**
	 * Recupere a entidade de banco de dados
	 * 
	 * @param query
	 * @return entidade de banco de dados
	 * @throws SQLException
	 */
	public IEntity queryUnique(IQuery query) throws SQLException;
	
	/**
	 * Recupere a entidade de banco de dados
	 * 
	 * @param entity
	 * @return identificador
	 * @throws SQLException
	 */
	public int insert(IEntity entity) throws SQLException;
	
	/**
	 * Recupere a entidade de banco de dados
	 * 
	 * @param entity
	 * @throws SQLException
	 */
	public void update(IEntity entity) throws SQLException;
	
	/**
	 * Recupere a entidade de banco de dados
	 * 
	 * @param query
	 * @throws SQLException
	 */
	public void delete(IQuery query) throws SQLException;
	
	/**
	 * Inicia uma transação
	 * 
	 * @throws SQLException
	 */
	public void begin() throws SQLException;
	
	/**
	 * Commit uma transação
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException;
	
	/**
	 * Finaliza uma transação
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException;
	
}
