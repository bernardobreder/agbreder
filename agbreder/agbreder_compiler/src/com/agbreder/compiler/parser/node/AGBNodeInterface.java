package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Indica que é um lvalue
 * 
 * @author bernardobreder
 */
public interface AGBNodeInterface {

	/**
	 * Semantica para Cabeçalhos
	 * 
	 * @param context
	 * @throws AGBException
	 */
	public void header(AGBCompileContext context) throws AGBException;

	/**
	 * Semantica para Cabeçalhos
	 * 
	 * @param context
	 * @throws AGBException
	 */
	public void body(AGBCompileContext context) throws AGBException;

	/**
	 * Lincagem para Cabeçalhos
	 * 
	 * @param context
	 * @throws AGBException
	 */
	public void link(AGBCompileContext context) throws AGBException;

	/**
	 * Semantica para Cabeçalhos
	 * 
	 * @param output
	 * @throws IOException
	 * @throws AGBException
	 */
	public void build(OpcodeOutputStream output) throws IOException, AGBException;

	/**
	 * Retorna o pai
	 * 
	 * @return pai
	 */
	public AGBNode getParent();

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(AGBNode parent);

	/**
	 * {@inheritDoc}
	 */
	public String toString();

}
