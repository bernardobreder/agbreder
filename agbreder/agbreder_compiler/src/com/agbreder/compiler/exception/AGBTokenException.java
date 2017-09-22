package com.agbreder.compiler.exception;

import com.agbreder.compiler.token.AGBToken;

/**
 * Erro lexical
 * 
 * @author bernardobreder
 */
public class AGBTokenException extends AGBException {

	/**
	 * Construtor
	 * 
	 * @param msg
	 * @param token
	 */
	public AGBTokenException(String msg, AGBToken token) {
		super(String.format("['%s','%s',%d,%d]: %s", token.getPath(), token.getImage(), token.getLine(), token.getColumn(), msg));
	}

	/**
	 * Erro de campo duplicado
	 * 
	 * @author bernardobreder
	 */
	public static class AGBDuplicateFieldTokenException extends AGBTokenException {

		/**
		 * Construtor
		 * 
		 * @param token
		 */
		public AGBDuplicateFieldTokenException(AGBToken token) {
			super("Duplicate Field", token);
		}

	}

	/**
	 * Erro de campo duplicado
	 * 
	 * @author bernardobreder
	 */
	public static class AGBDuplicateMethodTokenException extends AGBTokenException {

		/**
		 * Construtor
		 * 
		 * @param token
		 */
		public AGBDuplicateMethodTokenException(AGBToken token) {
			super("Duplicate Method", token);
		}

	}

	/**
	 * Erro de campo duplicado
	 * 
	 * @author bernardobreder
	 */
	public static class AGBReferenceTypeTokenException extends AGBTokenException {

		/**
		 * Construtor
		 * 
		 * @param token
		 */
		public AGBReferenceTypeTokenException(AGBToken token) {
			super(String.format("Can not found the Type '%s'", token.getImage()), token);
		}

	}

}
