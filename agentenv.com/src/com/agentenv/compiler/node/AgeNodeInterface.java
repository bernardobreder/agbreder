package com.agentenv.compiler.node;

import java.io.IOException;

import com.agentenv.compiler.linker.AgeContext;
import com.agentenv.compiler.linker.AgeLinker;

/**
 * Estrutura de um item de um código fonte
 * 
 * @author bernardobreder
 * 
 */
public interface AgeNodeInterface {

	/**
	 * @return the parent
	 */
	public AgeNodeInterface getParent();

	/**
	 * @param c
	 * @return the parent
	 */
	public <E extends AgeNodeInterface> E getParent(Class<E> c);

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(AgeNodeInterface parent);

	/**
	 * Realiza a geração de instrução
	 * 
	 * @param context
	 * @param linker
	 */
	public void build(AgeContext context, AgeLinker linker) throws IOException;

}
