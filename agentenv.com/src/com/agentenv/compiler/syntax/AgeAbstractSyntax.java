package com.agentenv.compiler.syntax;

import java.util.List;

import com.agentenv.compiler.lexical.AgeToken;
import com.agentenv.compiler.lexical.AgeTokenType;

public abstract class AgeAbstractSyntax {

	/** Tokens */
	public List<AgeToken> tokens;

	/** Indice */
	public int index;

	/** Caminho */
	private final String filename;

	/**
	 * Construtor
	 * 
	 * @param filename
	 * @param tokens
	 */
	public AgeAbstractSyntax(String filename, List<AgeToken> tokens) {
		this.filename = filename;
		this.tokens = tokens;
		this.index = 0;
	}

	/**
	 * Verifica se chegou no final dos tokens
	 * 
	 * @return boolean
	 */
	public boolean isEof() {
		return isEof(0);
	}

	/**
	 * Verifica se chegou no final dos tokens
	 * 
	 * @param index
	 * @return boolean
	 */
	public boolean isEof(int index) {
		return this.index + index >= this.tokens.size();
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @param type
	 * @return boolean
	 */
	public boolean is(AgeTokenType type) {
		return is(type, 0);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @param type
	 * @param index
	 * @return boolean
	 */
	public boolean is(AgeTokenType type, int index) {
		return !isEof(index) && get(index).getType() == type.getId();
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @param index
	 * @return boolean
	 */
	public AgeToken get(int index) {
		return this.tokens.get(this.index + index);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @return boolean
	 */
	public boolean isId() {
		return is(AgeTokenType.ID);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @param index
	 * @return boolean
	 */
	public boolean isId(int index) {
		return is(AgeTokenType.ID, index);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro. Se for,
	 * será incrementado o contador de token
	 * 
	 * @param type
	 * @return boolean
	 */
	public boolean can(AgeTokenType type) {
		return can(type, 0);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro. Se for,
	 * será incrementado o contador de token
	 * 
	 * @param type
	 * @param index
	 * @return boolean
	 */
	public boolean can(AgeTokenType type, int index) {
		if (is(type, index)) {
			this.index++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Exige que o token corrente seja do tipo id
	 * 
	 * @return token
	 * @throws AgeSyntaxException
	 */
	public AgeToken doId() throws AgeSyntaxException {
		return check(AgeTokenType.ID);
	}

	/**
	 * Exige que um token esteja corrente
	 * 
	 * @param type
	 * @return token
	 * @throws AgeSyntaxException
	 */
	public AgeToken check(AgeTokenType type) throws AgeSyntaxException {
		if (!can(type)) {
			String expected = type.getName();
			if (isEof()) {
				throw error(expected, new AgeToken(filename, "<EOF>", -1, -1, -1));
			}
			AgeToken token = get(0);
			AgeToken atual = token;
			throw error(expected, atual);
		}
		return this.tokens.get(this.index - 1);
	}

	/**
	 * Objeto de erro
	 * 
	 * @param expected
	 * @param atual
	 * @return exception
	 */
	public AgeSyntaxException error(String expected, AgeToken token) {
		String msg = String.format("Expected '%s' and not '%s'", expected, token.getImage());
		msg = String.format("['%s','%s',%d,%d]: %s", token.getPath(), token.getImage(), token.getLine(), token.getColumn(), msg);
		return new AgeSyntaxException(msg);
	}
}
