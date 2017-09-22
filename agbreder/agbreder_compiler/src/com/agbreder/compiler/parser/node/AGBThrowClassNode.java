package com.agbreder.compiler.parser.node;

/**
 * Classe do tipo Throw
 * 
 * @author bernardobreder
 */
public class AGBThrowClassNode extends AGBClassNode {

	/** Indica se é um erro em tempo de execução */
	private boolean runtime;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param name
	 * @param cmds
	 * @param extendToken
	 */
	public AGBThrowClassNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * @return the runtime
	 */
	public boolean isRuntime() {
		return runtime;
	}

	/**
	 * @param runtime
	 *            the runtime to set
	 */
	public void setRuntime(boolean runtime) {
		this.runtime = runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isThrow() {
		return true;
	}

}
