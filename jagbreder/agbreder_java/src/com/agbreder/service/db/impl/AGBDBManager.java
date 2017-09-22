package com.agbreder.service.db.impl;

import java.sql.SQLException;
import java.util.List;

import com.agbreder.api.db.IDBManager;
import com.agbreder.api.db.IEntity;
import com.agbreder.api.db.IQuery;

/**
 * Gerenciador de Banco de Dados
 * 
 * @author bernardobreder
 */
public class AGBDBManager implements IDBManager {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<IEntity> query(IQuery query) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IEntity queryUnique(IQuery query) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert(IEntity entity) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(IEntity entity) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(IQuery query) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void begin() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
}
